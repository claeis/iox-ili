package ch.interlis.iox_j.validator.functions;

import ch.ehi.iox.objpool.impl.ObjPoolImpl2;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.Projection;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ObjectPool;
import ch.interlis.iox_j.validator.ObjectPoolKey;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectPoolFunctions {
    private final Validator validator;
    private final ObjectPool objectPool;
    private final LogEventFactory logger;

    public static final String OBJECTPOOL = "ObjectPool";

    public ObjectPoolFunctions(Validator validator, ObjectPool objectPool, LogEventFactory logger) {
        this.validator = validator;
        this.objectPool = objectPool;
        this.logger = logger;
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
        } else if (currentFunction.getName().equals("coalesceNumeric")) {
            return evaluateCoalesce(validationKind, usageScope, iomObj, actualArguments);
        } else {
            return Value.createNotYetImplemented();
        }
    }

    private Value evaluateAllObjects(String validationKind, String usageScope, IomObject iomObj, Value[] actualArguments) {
        Viewable<?> viewable = actualArguments[0].getViewable();

        if (viewable == null) {
            logger.logErrorMsg("Missing ObjectClass argument for allObjects");
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

    private Value evaluateCoalesce(String validationKind, String usageScope, IomObject iomObj, Value[] actualArguments) {
        Value input = actualArguments[0];
        Value defaultValue = actualArguments[1];

        if (input.isUndefined()) {
            return defaultValue;
        } else {
            return input;
        }
    }
}
