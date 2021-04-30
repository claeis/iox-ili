package ch.interlis.iox_j.validator.functions;

import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.validator.Value;
import ch.interlis.iox_j.validator.Validator;

public class MinimalRuntimeSystem {
    
    private TransferDescription td = null;
    private ch.interlis.iox.IoxValidationConfig validationConfig = null;
    private Validator validator=null;
    
    public MinimalRuntimeSystem(Validator validator, TransferDescription td, ch.interlis.iox.IoxValidationConfig validationConfig) {
        this.td = td;
        this.validationConfig = validationConfig;
        this.validator=validator;
    }
    
    public Value evaluateFunction(Function currentFunction, FunctionCall functionCallObj, IomObject parentObject, 
            String validationKind, String usageScope, IomObject iomObj, TextType texttype, RoleDef firstRole) {
        
        
        if (currentFunction.getScopedName(null).equals("MinimalRuntimeSystem01.getParameterValue")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            for (Evaluable anArgument : arguments) {
                Value arg = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, anArgument,
                        firstRole);
                if (arg.skipEvaluation()) {
                    return arg;
                }
                if (arg.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                String value=(String)td.getActualRuntimeParameter(arg.getValue());
                if(value!=null) {
                    return new Value(texttype, value);
                }
                return Value.createUndefined();
            }
            return null;
        }
        return null;
    }

}
