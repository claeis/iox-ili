package ch.interlis.iox_j.validator.functions;

import java.util.Iterator;
import java.util.Map;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.Objects;
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
    
    public Interlis_ext(Validator validator, TransferDescription td, ch.interlis.iox.IoxValidationConfig validationConfig) {
        this.td = td;
        this.validationConfig = validationConfig;
        this.validator=validator;
    }
    
    public Value evaluateFunction(Function currentFunction, IomObject parentObject,
            String validationKind, String usageScope, IomObject iomObj,
            Evaluable expression, Map<Evaluable, Value> functions, TransferDescription td, RoleDef firstRole) {

        
        if (currentFunction.getScopedName(null).equals("INTERLIS_ext.areAreas2") || currentFunction.getScopedName(null).equals("INTERLIS_ext.areAreas3")){
            /*
             * FUNCTION areAreas2(
             *     Object: OBJECT OF ANYCLASS;
             *     SurfaceBag: TEXT;
             *     SurfaceAttr: TEXT): BOOLEAN;
             * 
             *   FUNCTION areAreas3(
             *     Objects: OBJECTS OF ANYCLASS;
             *     SurfaceBag: TEXT;
             *     SurfaceAttr: TEXT): BOOLEAN;
             */
            FunctionCall functionCall = (FunctionCall) expression;
            Evaluable[] arguments = functionCall.getArguments();
            // argument Objects (list<IomObjects)
            Value argObjects=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[0], firstRole);
            if (argObjects.skipEvaluation()){
                return argObjects;
            }
            if (argObjects.isUndefined()){
                return Value.createSkipEvaluation();
            }
            // argument SurfaceBag
            Value argSurfaceBag=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
            if (argSurfaceBag.skipEvaluation()){
                return argSurfaceBag;
            }
            Viewable currentClass=null;
            if(iomObj!=null) {
                currentClass=(Viewable) td.getElement(iomObj.getobjecttag());
            }else if(argObjects.getViewable()!=null) {
                currentClass=argObjects.getViewable();
            }else if(argObjects.getComplexObjects()!=null) {
                Iterator<IomObject> it = argObjects.getComplexObjects().iterator();
                if(!it.hasNext()) {
                    return new Value(true);
                }
                currentClass=(Viewable) td.getElement(it.next().getobjecttag());
            }else {
                throw new IllegalStateException("unkown class in "+usageScope);
            }
            ObjectPath surfaceBagObjPath = null;
            PathEl surfaceBagPath[] = null;
            Viewable viewable = null;
            if(!argSurfaceBag.isUndefined()) {
                try {
                    surfaceBagObjPath = validator.parseObjectOrAttributePath(currentClass, argSurfaceBag.getValue());
                    if (surfaceBagObjPath.getPathElements() != null) {
                        PathEl surfaceBagPathEl[] = surfaceBagObjPath.getPathElements();
                        surfaceBagPath = surfaceBagPathEl;              
                        viewable = surfaceBagObjPath.getViewable();
                    }
                } catch (Ili2cException e) {
                    EhiLogger.logError(e);
                }
            }
            // argument SurfaceAttr
            Value argSurfaceAttr=validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[2], firstRole);
            if (argSurfaceAttr.skipEvaluation()){
                return argSurfaceAttr;
            }
            if (argSurfaceAttr.isUndefined()){
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
                surfaceAttrObjPath = validator.parseObjectOrAttributePath(attrObjClass, argSurfaceAttr.getValue());
                if (surfaceAttrObjPath.getPathElements() != null) {
                    PathEl surfaceAttrPathEl[] = surfaceAttrObjPath.getPathElements();
                    surfaceAttrPath = surfaceAttrPathEl;
                }
            } catch (Ili2cException e) {
                EhiLogger.logError(e);
            }                    

            // use cached value if it exists and first argument (Objects) is 'ALL'
            if (functions.containsKey(functionCall) && arguments[0] instanceof Objects) {
                return functions.get(functionCall);
            }

            Value isArea = validator.evaluateAreArea(iomObj, argObjects, surfaceBagPath, surfaceAttrPath, currentFunction, validationKind);
            functions.put(functionCall, isArea);
            return isArea;
        }
        
        
        return null;
    }
}
