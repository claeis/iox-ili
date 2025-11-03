package ch.interlis.iox_j.validator.functions;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.types.OutParam;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ElementsFunctions {
    private static final String FUNCTION_VALUES_OF_PATH = "valuesOfPath";
    private static final String FUNCTION_VALUES_OF_PATH_N = "valuesOfPathN";
    private static final String FUNCTION_EXISTS_IN_LIST = "existsInList";
    private static final String FUNCTION_EXISTS_IN_LIST_N = "existsInListN";
    private static final String FUNCTION_VALUES = "values";
    private static final String FUNCTION_VALUES_N = "valuesN";
    private static final String FUNCTION_COALESCE = "coalesce";
    private static final String FUNCTION_COALESCE_N = "coalesceN";

    public static final String ELEMENTS_V1_0 = "Elements_V1_0";

    private final TransferDescription td;
    private final IoxValidationConfig validationConfig;
    private final Validator validator;
    private final LogEventFactory logger;

    public ElementsFunctions(Validator validator, TransferDescription td, IoxValidationConfig validationConfig, LogEventFactory logger) {
        this.validator = validator;
        this.td = td;
        this.validationConfig = validationConfig;

        this.logger = logger;
        logger.setValidationConfig(validationConfig);
    }

    public Value evaluateFunction(Function currentFunction, FunctionCall functionCallObj, IomObject parentObject,
                                  String validationKind, String usageScope, IomObject iomObj, RoleDef firstRole) {

        Evaluable[] arguments = functionCallObj.getArguments();
        Value[] actualArguments = new Value[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            Value result = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[i], firstRole);
            if (result.skipEvaluation()) {
                return result;
            }

            actualArguments[i] = result;
        }

        if (currentFunction.getName().equals(FUNCTION_VALUES_OF_PATH)) {
            return evaluateValuesOfPath(validationKind, usageScope, iomObj, actualArguments);
        }else if (currentFunction.getName().equals(FUNCTION_VALUES_OF_PATH_N)) {
                return evaluateValuesOfPath(validationKind, usageScope, iomObj, actualArguments);
        }else if (currentFunction.getName().equals(FUNCTION_EXISTS_IN_LIST)) {
            return evaluateExistsInList(validationKind, usageScope, iomObj, actualArguments);
        }else if (currentFunction.getName().equals(FUNCTION_EXISTS_IN_LIST_N)) {
            return evaluateExistsInList(validationKind, usageScope, iomObj, actualArguments);
        }else if (currentFunction.getName().equals(FUNCTION_VALUES)) {
            return evaluateValues(validationKind, usageScope, iomObj, actualArguments,new ch.interlis.ili2c.metamodel.TextType());
        }else if (currentFunction.getName().equals(FUNCTION_VALUES_N)) {
            return evaluateValues(validationKind, usageScope, iomObj, actualArguments,new ch.interlis.ili2c.metamodel.NumericType());
        }else if (currentFunction.getName().equals(FUNCTION_COALESCE)) {
            return evaluateCoalesce(validationKind, usageScope, iomObj, actualArguments);
        }else if (currentFunction.getName().equals(FUNCTION_COALESCE_N)) {
            return evaluateCoalesce(validationKind, usageScope, iomObj, actualArguments);
        } else {
            return Value.createNotYetImplemented();
        }
    }
    //FUNCTION valuesOfPath(attrPath: TEXT): BAG OF TEXT;
    //FUNCTION valuesOfPathN(attrPath: TEXT): BAG OF NUMERIC;
    private Value evaluateValuesOfPath(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        if(actualArguments[0].isUndefined()) {
            return Value.createUndefined();
        }
        String attrPathArg=actualArguments[0].getValue();
        Viewable currentClass=(Viewable) td.getElement(mainObj.getobjecttag());
        
        PathEl attrPath[] = null;
        ObjectPath attrObjPath = null;
        try {
            attrObjPath = validator.parseObjectOrAttributePath(currentClass, attrPathArg);
            if (attrObjPath.getPathElements() != null) {
                PathEl surfaceAttrPathEl[] = attrObjPath.getPathElements();
                attrPath = surfaceAttrPathEl;
            }
        } catch (Ili2cException e) {
            EhiLogger.logError(e);
        }                    
        
        Value valueOfObjectPath=validator.getValueFromObjectPath(null, mainObj, attrPath, null);
        return valueOfObjectPath;
    }
    //FUNCTION existsInList(value: TEXT;list: BAG OF TEXT): BOOLEAN;
    //FUNCTION existsInListN(value: NUMERIC;list: BAG OF NUMERIC): BOOLEAN;
    private Value evaluateExistsInList(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        if(actualArguments[0].isUndefined()) {
            return Value.createUndefined();
        }
        if(actualArguments[1].isUndefined()) {
            return new Value(false);
        }
        String valueArg=actualArguments[0].getValue();
        
        Value listArg=actualArguments[1];
        if (listArg.getValues() != null) {
            String[] values = listArg.getValues();
            Arrays.sort(values);
            if(Arrays.binarySearch(values,valueArg)>=0) {
                return new Value(true);
            }
        }else if (listArg.getValue() != null) {
            if(valueArg.equals(listArg.getValue())) {
                return new Value(true);
            }
        }
        return new Value(false);
    }
    //FUNCTION values(list: TEXT): LIST OF TEXT;
    //FUNCTION valuesN(list: TEXT): LIST OF NUMERIC;
    private Value evaluateValues(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments,Type type) {
        if(actualArguments[0].isUndefined()) {
            return Value.createUndefined();
        }
        String listArg=actualArguments[0].getValue();
        String values[]=listArg.split(";");
        Value ret=new Value(type,values);
        return ret;
    }
    // FUNCTION coalesce(inputValue: TEXT; defaultValue: TEXT): TEXT;
    // FUNCTION coalesceN(inputValue: NUMERIC; defaultValue: NUMERIC): NUMERIC;
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
