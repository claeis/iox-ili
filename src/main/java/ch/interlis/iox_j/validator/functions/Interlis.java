package ch.interlis.iox_j.validator.functions;

import java.util.Iterator;
import java.util.Map;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;

public class Interlis {

    private TransferDescription td = null;
    private ch.interlis.iox.IoxValidationConfig validationConfig = null;
    private Validator validator=null;
    private Iterator<String> allObjIterator=null;
    
    public Interlis(Validator validator, TransferDescription td, ch.interlis.iox.IoxValidationConfig validationConfig, Iterator<String> allObjIterator) {
        this.td = td;
        this.validationConfig = validationConfig;
        this.validator=validator;
        this.allObjIterator=allObjIterator;
    }
    
    public Value evaluateFunction(Function currentFunction, FunctionCall functionCallObj, IomObject parentObject,
            String validationKind, String usageScope, IomObject iomObj, TextType texttype, 
            Evaluable expression, Map<Function, Value> functions, TransferDescription td, RoleDef firstRole) {
        
        if(currentFunction.getScopedName(null).equals("INTERLIS.len") || currentFunction.getScopedName(null).equals("INTERLIS.lenM")){
            Evaluable[] arguments = functionCallObj.getArguments();
            for(Evaluable anArgument : arguments){
                Value arg=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,anArgument, firstRole);
                if (arg.skipEvaluation()){
                    return arg;
                    }
                if (arg.isUndefined()){
                    return Value.createSkipEvaluation();
                }
                if(arg.getValue()!=null){
                    int lengthOfArgument = arg.getValue().length();
                    return new Value(lengthOfArgument);
                }
            }
            return new Value(false);
        } else if(currentFunction.getScopedName(null).equals("INTERLIS.trim") || currentFunction.getScopedName(null).equals("INTERLIS.trimM")){
            Evaluable[] arguments = functionCallObj.getArguments();
            for(Evaluable anArgument : arguments){
                Value arg=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,anArgument, firstRole);
                if (arg.skipEvaluation()){
                    return arg;
                }
                if (arg.isUndefined()){
                    return Value.createSkipEvaluation();
            }
                return new Value(texttype, arg.getValue().trim());
            }
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.isEnumSubVal")){
            Evaluable[] arguments = functionCallObj.getArguments();
            Value subEnum=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (subEnum.skipEvaluation()){
                return subEnum;
            }
            if (subEnum.isUndefined()){
                return Value.createSkipEvaluation();
            }
            Value nodeEnum=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
            if (nodeEnum.skipEvaluation()){
                return nodeEnum;
            }
            if (nodeEnum.isUndefined()){
                return Value.createSkipEvaluation();
            }
            if(subEnum.getValue().toString().startsWith(nodeEnum.getValue().toString())){
                return new Value(true);
            } else {
                return new Value(false);
            }
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.inEnumRange")){
            Evaluable[] arguments = functionCallObj.getArguments();
            Value enumToCompare=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (enumToCompare.skipEvaluation()){
                return enumToCompare;
            }
            if (enumToCompare.isUndefined()){
                return Value.createSkipEvaluation();
            }
            Value minEnum=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
            if (minEnum.skipEvaluation()){
                return minEnum;
            }
            if (minEnum.isUndefined()){
                return Value.createSkipEvaluation();
            }
            Value maxEnum=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[2], firstRole);
            if (maxEnum.skipEvaluation()){
                return maxEnum;
            }
            if (maxEnum.isUndefined()){
                return Value.createSkipEvaluation();
            }
            if (enumToCompare.getType() instanceof EnumerationType){
                EnumerationType enumerationType = (EnumerationType) enumToCompare.getType();
                    // enumeration has to be ordered
                    if(enumerationType.isOrdered()){
                    // enumerations from same enumeration
                    if(enumToCompare.getRefTypeName().equals(minEnum.getRefTypeName()) && (enumToCompare.getRefTypeName().equals(maxEnum.getRefTypeName()))){
                        int indexOfEnumToCompare = enumerationType.getValues().indexOf(enumToCompare.getValue());
                        int indexOfMinEnumValue = enumerationType.getValues().indexOf(minEnum.getValue());
                        int indexOfMaxEnumValue = enumerationType.getValues().indexOf(maxEnum.getValue());
                        // enum is between min and max
                        if(indexOfEnumToCompare > indexOfMinEnumValue && indexOfEnumToCompare < indexOfMaxEnumValue){
                            return new Value(true);
                        }
                    }
                }
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.objectCount")){
            FunctionCall functionCall = (FunctionCall) expression;
            Evaluable[] arguments = functionCall.getArguments();
            Value value=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (value.skipEvaluation()){
                return value;
            }
            if (value.isUndefined()){
                return new Value(0);
            }
            if(value.getComplexObjects()!=null){
                return new Value(value.getComplexObjects().size());
            }else if(value.getViewable()!=null) {
                for(Function aFunction:functions.keySet()) {
                    // contains/equal would not work here, because it is an object compare.
                    if(aFunction==currentFunction) {
                        Value objCount=functions.get(currentFunction);
                        return objCount;
                    }
                }
                Value objectCount=null;
                objectCount = validator.evaluateObjectCount(value);
                // put the result of object count as value to the current function.
                functions.put(currentFunction, objectCount);
                return objectCount;
            }
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.elementCount")){
            FunctionCall functionCall = (FunctionCall) expression;
            Evaluable[] arguments = (Evaluable[]) functionCall.getArguments();
            Evaluable anArgument = (Evaluable) arguments[0];
            Value value=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, anArgument, firstRole);
            if (value.skipEvaluation()){
                return value;
            }
            if (value.isUndefined()){
                return Value.createSkipEvaluation();
            }
            return new Value(value.getComplexObjects().size());
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.isOfClass")){
            FunctionCall functionCall = (FunctionCall) expression;
            Evaluable[] arguments = functionCall.getArguments();
            Value paramObject=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (paramObject.skipEvaluation()){
                return paramObject;
            }
            if (paramObject.isUndefined()){
                return Value.createSkipEvaluation();
            }
            Value paramClass=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
            if (paramClass.skipEvaluation()){
                return paramClass;
            }
            if (paramClass.isUndefined()){
                return Value.createSkipEvaluation();
            }
            Viewable paramObjectViewable = paramObject.getViewable();
            if (paramObject.getComplexObjects() != null) {
                paramObjectViewable=(Viewable)td.getElement(paramObject.getComplexObjects().iterator().next().getobjecttag());                  
            }
            if(paramObjectViewable.equals(paramClass.getViewable())){
                return new Value(true);
            }
            if(paramObjectViewable.isExtending(paramClass.getViewable())){
                return new Value(true);                 
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.isSubClass")){
            FunctionCall functionCall = (FunctionCall) expression;
            Evaluable[] arguments = functionCall.getArguments();
            Value subViewable=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (subViewable.skipEvaluation()){
                return subViewable;
            }
            if (subViewable.isUndefined()){
                return Value.createSkipEvaluation();
            }
            Value superViewable=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
            if (superViewable.skipEvaluation()){
                return superViewable;
            }
            if (superViewable.isUndefined()){
                return Value.createSkipEvaluation();
            }
            if(subViewable.getViewable().equals(superViewable.getViewable())){
                return new Value(true);
            }
            if(superViewable.getViewable().isExtending(subViewable.getViewable())){
                return new Value(true);                 
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.myClass")){
            FunctionCall functionCall = (FunctionCall) expression;
            Evaluable[] arguments = functionCall.getArguments();
            Value targetViewable=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (targetViewable.skipEvaluation()){
                return targetViewable;
            }
            if (targetViewable.isUndefined()){
                return Value.createSkipEvaluation();
            }
            return new Value((Viewable)td.getElement(targetViewable.getComplexObjects().iterator().next().getobjecttag()));
        } else if (currentFunction.getScopedName(null).equals("INTERLIS.areAreas")){
            FunctionCall functionCall = (FunctionCall) expression;
            Evaluable[] arguments = functionCall.getArguments();
            // founded objects (list<IomObjects)
            Value value=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (value.skipEvaluation()){
                return value;
            }
            if (value.isUndefined()){
                return Value.createSkipEvaluation();
            }
            // count of objects condition returns attrName of BAG / undefined=(numericIsDefined=false)
            Value surfaceBag=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
            if (surfaceBag.skipEvaluation()){
                return surfaceBag;
            }
            Viewable currentClass=(Viewable) td.getElement(iomObj.getobjecttag());
            //PathEl surfaceBagPath[]=parseObjectPath(currentClass, surfaceBag.getValue());
            ObjectPath surfaceBagObjPath = null;
            PathEl surfaceBagPath[] = null;
            Viewable viewable = null;
            if(!surfaceBag.isUndefined()) {
                try {
                    surfaceBagObjPath = validator.parseObjectOrAttributePath(currentClass, surfaceBag.getValue());
                    if (surfaceBagObjPath.getPathElements() != null) {
                        PathEl surfaceBagPathEl[] = surfaceBagObjPath.getPathElements();
                        surfaceBagPath = surfaceBagPathEl;          
                        viewable = surfaceBagObjPath.getViewable();
                    }
                } catch (Ili2cException e) {
                    EhiLogger.logError(e);
                }
            }
            // name of surface (textType)
            Value surfaceAttr=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[2], firstRole);
            if (surfaceAttr.skipEvaluation()){
                return surfaceAttr;
            }
            if (surfaceAttr.isUndefined()){
                return Value.createSkipEvaluation();
            }
            
            Viewable attrObjClass = null;
            if (viewable != null) {
                attrObjClass = viewable;
            } else {
                attrObjClass = currentClass;
            }               
            
            ObjectPath surfaceAttrObjPath;
            PathEl surfaceAttrPath[] = null;
            try {
                surfaceAttrObjPath = validator.parseObjectOrAttributePath(attrObjClass, surfaceAttr.getValue());
                if (surfaceAttrObjPath.getPathElements() != null) {
                    PathEl surfaceAttrPathEl[] = surfaceAttrObjPath.getPathElements(); 
                    surfaceAttrPath = surfaceAttrPathEl;                        
                }
            } catch (Ili2cException e) {
                EhiLogger.logError(e);
            }
            //parsePath(surfaceAttr.getValue())
            for(Function aFunction:functions.keySet()) {
                if(aFunction==currentFunction) {
                    Value isArea=functions.get(currentFunction);
                    return isArea;
                }
            }
            Value isArea = validator.evaluateAreArea(iomObj, value, surfaceBagPath, surfaceAttrPath, currentFunction);
            functions.put(currentFunction, isArea);
            return isArea;
        }
        
        return null;

    }
    
}
