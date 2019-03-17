package ch.interlis.iox_j.validator.functions;

import java.util.Iterator;
import java.util.Map;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;

public class Interlis_ext {
    
    private TransferDescription td = null;
    private ch.interlis.iox.IoxValidationConfig validationConfig = null;
    private Validator validator=null;
    private Iterator<String> allObjIterator=null;
    
    public Interlis_ext(Validator validator, TransferDescription td, ch.interlis.iox.IoxValidationConfig validationConfig, Iterator<String> allObjIterator) {
        this.td = td;
        this.validationConfig = validationConfig;
        this.validator=validator;
        this.allObjIterator=allObjIterator;
    }
    
    public Value evaluateFunction(Function currentFunction, IomObject parentObject,
            String validationKind, String usageScope, IomObject iomObj,
            Evaluable expression, Map<Function, Value> functions, TransferDescription td, RoleDef firstRole) {

        
        if (currentFunction.getScopedName(null).equals("INTERLIS_ext.areAreas2") || currentFunction.getScopedName(null).equals("INTERLIS_ext.areAreas3")){
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
//            PathEl surfaceBagPath[]=parseObjectPath(currentClass, surfaceBag.getValue());
            ObjectPath surfaceBagObjPath = null;
            PathEl surfaceBagPath[] = null;
            Viewable viewable = null;
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
            
            PathEl surfaceAttrPath[] = null;
            ObjectPath surfaceAttrObjPath = null;
            try {
                surfaceAttrObjPath = validator.parseObjectOrAttributePath(attrObjClass, surfaceAttr.getValue());
                if (surfaceAttrObjPath.getPathElements() != null) {
                    PathEl surfaceAttrPathEl[] = surfaceAttrObjPath.getPathElements();
                    surfaceAttrPath = surfaceAttrPathEl;
                }
            } catch (Ili2cException e) {
                EhiLogger.logError(e);
            }                    

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
