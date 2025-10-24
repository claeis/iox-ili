package ch.interlis.iox_j.validator.functions;

import ch.ehi.iox.objpool.impl.ObjPoolImpl2;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.Projection;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.parser.Ili23Parser;
import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ObjectPool;
import ch.interlis.iox_j.validator.ObjectPoolKey;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ObjectPoolFunctions {
    private final Validator validator;
    private final ObjectPool objectPool;
    private final LogEventFactory logger;
    private final TransferDescription td;

    public static final String OBJECTPOOL = "ObjectPool_V1_0";

    public ObjectPoolFunctions(Validator validator, TransferDescription td, ObjectPool objectPool, LogEventFactory logger) {
        this.validator = validator;
        this.objectPool = objectPool;
        this.logger = logger;
        this.td = td;
    }

    public Value evaluateFunction(Function currentFunction, FunctionCall functionCallObj, IomObject parentObject,
                                  String validationKind, String usageScope, IomObject iomObj, TextType texttype, RoleDef firstRole) {
        Evaluable[] arguments = functionCallObj.getArguments();
        Value[] actualArguments = new Value[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            Value result = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[i], firstRole);
            if (result.skipEvaluation()) {
                return result;
            }

            actualArguments[i] = result;
        }

        if (currentFunction.getName().equals("allObjects")) {
            return evaluateAllObjects(validationKind, usageScope, iomObj, actualArguments);
        } else if (currentFunction.getName().equals("filter")) {
            return evaluateFilter(validationKind, usageScope, iomObj, actualArguments);
        } else {
            return Value.createNotYetImplemented();
        }
    }

    private Value evaluateAllObjects(String validationKind, String usageScope, IomObject iomObj, Value[] actualArguments) {
        Viewable<?> viewable = actualArguments[0].getViewable();

        if (viewable == null) {
            logger.addEvent(logger.logErrorMsg("Missing ObjectClass argument for allObjects"));
            return Value.createUndefined();
        }

        Projection projection = null;
        Viewable<?> objectClass = viewable;
        if (viewable instanceof Projection) {
            projection = (Projection) viewable;
            objectClass = projection.getSelected().getAliasing();
        }

        String className = objectClass.getScopedName();
        List<IomObject> objects = new ArrayList<IomObject>();
        for (String basketId : objectPool.getDataBids()) {
            ObjPoolImpl2<ObjectPoolKey, IomObject> basketObjectPool = objectPool.getObjectsOfBasketId(basketId);
            Iterator<IomObject> valueIterator = basketObjectPool.valueIterator();
            while (valueIterator.hasNext()) {
                IomObject object = valueIterator.next();
                if (object.getobjecttag().equals(className) && (projection == null || validator.viewIncludesObject(projection, object))) {
                    objects.add(object);
                }
            }
        }
        return new Value(objects);
    }

    private Value evaluateFilter(String validationKind, String usageScope, IomObject iomObect, Value[] actualArguments) {
        Value argObjects = actualArguments[0];
        Value argFilter = actualArguments[1];

        if (argObjects.isUndefined() || argObjects.getComplexObjects() == null || argFilter.isUndefined() || argFilter.getValue() == null) {
            return Value.createUndefined();
        }

        Collection<IomObject> objects = argObjects.getComplexObjects();
        if (objects.isEmpty()) {
            return argObjects;
        }

        Viewable<?> objectClass = getCommonBaseClass(td, objects);
        if (objectClass == null) {
            logger.addEvent(logger.logErrorMsg("Objects have no common base class in " + usageScope));
            return Value.createUndefined();
        }

        String expression = resolveAttributesInExpression(iomObect, argFilter.getValue());
        logger.addEvent(logger.logDetailInfoMsg("Resolved filter expression: " + expression));
        if (expression == null) {
            return Value.createUndefined();
        }

        final Evaluable filter = parseLogicExpression(objectClass, expression, usageScope);
        if (filter == null) {
            return Value.createUndefined();
        }

        List<IomObject> filteredObjects = objects.stream()
            .filter(object -> {
                Value value = validator.evaluateExpression(null, validationKind, usageScope, object, filter, null);
                return !value.skipEvaluation() && value.isTrue();
            })
            .collect(Collectors.toList());

        return new Value(filteredObjects);
    }

    private Evaluable parseLogicExpression(Viewable<?> viewable, String expression, String usageScope) {
        Type booleanType = Type.findReal(td.INTERLIS.BOOLEAN.getType());
        try {
            return Ili23Parser.parseExpression(td, expression, viewable, booleanType, null);
        } catch (Ili2cException e) {
            logger.addEvent(logger.logErrorMsg("Failed to parse filter expression in " + usageScope + ": " + e.getMessage()));
            return null;
        }
    }

    /**
     * Get the common base class of the given {@code objects}.
     *
     * @param td the {@link TransferDescription} instance.
     * @param objects the collection of {@link IomObject} to find the common base class of.
     *
     * @return the common base class of the given {@code objects} or {@code null} if no common base class could be found.
     */
    private static Viewable<?> getCommonBaseClass(TransferDescription td, Collection<IomObject> objects) {
        if (objects.isEmpty()) {
            return null;
        }

        Set<String> classNames = objects.stream()
                .map(IomObject::getobjecttag)
                .collect(Collectors.toSet());
        Viewable<?> firstClass = (Viewable<?>) td.getElement(classNames.iterator().next());
        if (classNames.size() == 1) {
            return firstClass;
        }

        return classNames.stream()
                .map(className -> (Viewable) td.getElement(className))
                .reduce(firstClass, ObjectPoolFunctions::getCommonBaseClass);
    }

    private static Viewable<?> getCommonBaseClass(Viewable<?> classA, Viewable<?> classB) {
        if (classA == null || classB == null) {
            return null;
        }
        Viewable<?> currentClass = classA;
        while (currentClass != null) {
            if (currentClass == classB || classB.isExtending(currentClass)) {
                return currentClass;
            }
            currentClass = (Viewable<?>) currentClass.getExtending();
        }
        return null;
    }

    /**
     * Resolves an expression by replacing placeholders with attribute values from an IomObject.
     * Placeholders are expected in the format {attributeName}.
     *
     * @param iomObject  The IomObject to get attribute values from.
     * @param expression The expression string with placeholders.
     * @return The resolved expression string or <code>null</code> if anything went wrong.
     */
    private String resolveAttributesInExpression(IomObject iomObject, String expression) {
        if (expression == null || iomObject == null) {
            return expression;
        }

        Element element = td.getElement(iomObject.getobjecttag());
        if (!(element instanceof Viewable<?>)) {
            logger.addEvent(logger.logErrorMsg("Element '" + iomObject.getobjecttag() + "' is not a Viewable type."));
            return null;
        }
        Viewable<?> viewable = (Viewable<?>) element;

        Pattern pattern = Pattern.compile("\\{([^}]+)}");
        Matcher matcher = pattern.matcher(expression);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String attrName = matcher.group(1);
            AttributeDef attributeDef = viewable.findAttribute(attrName);
            if (attributeDef == null) {
                logger.addEvent(logger.logErrorMsg("Could not find attribute '" + attrName + "' in class '" + viewable.getScopedName() + "'"));
                return null;
            }
            Type type = attributeDef.getDomainResolvingAll();
            String attrValue = iomObject.getattrvalue(attrName);

            if (type instanceof EnumerationType) {
                attrValue = attrValue == null ? "UNDEFINED" : "#" + attrValue;
            } else if (type instanceof TextType) {
                attrValue = attrValue == null ? "UNDEFINED" : '"' + attrValue + '"';
            } else if (type instanceof NumericType) {
                attrValue = attrValue == null ? "UNDEFINED" : attrValue;
            } else {
                logger.addEvent(logger.logErrorMsg("Unsupported attribute type for attribute '" + attrName + "' in class '" + viewable.getScopedName() + "'"));
                return null;
            }

            // Escape backslashes and dollar signs for appendReplacement
            String replacement = Matcher.quoteReplacement(attrValue);
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
