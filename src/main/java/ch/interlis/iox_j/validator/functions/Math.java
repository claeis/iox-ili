package ch.interlis.iox_j.validator.functions;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;

public class Math {
    
    private TransferDescription td = null;
    private ch.interlis.iox.IoxValidationConfig validationConfig = null;
    private Validator validator=null;
    
    public Math(Validator validator, TransferDescription td, ch.interlis.iox.IoxValidationConfig validationConfig) {
        this.td = td;
        this.validationConfig = validationConfig;
        this.validator=validator;
    }

    public Value evaluateFunction(Function currentFunction, FunctionCall functionCallObj, IomObject parentObject,
            String validationKind, String usageScope, IomObject iomObj, TextType texttype, RoleDef firstRole) {
        
        if (currentFunction.getScopedName(null).equals("Math.add")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (secondValue.skipEvaluation()) {
                    return secondValue;
                }
                if (secondValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null && secondValue.getValue() != null) {
                    Integer value1 = Integer.parseInt(firstValue.getValue());
                    Integer value2 = Integer.parseInt(secondValue.getValue());
                    return new Value(value1 + value2);
                }

            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.sub")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (secondValue.skipEvaluation()) {
                    return secondValue;
                }
                if (secondValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null && secondValue.getValue() != null) {
                    Integer value1 = Integer.parseInt(firstValue.getValue());
                    Integer value2 = Integer.parseInt(secondValue.getValue());
                    return new Value(value1 - value2);
                }

            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.mul")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (secondValue.skipEvaluation()) {
                    return secondValue;
                }
                if (secondValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null && secondValue.getValue() != null) {
                    Integer value1 = Integer.parseInt(firstValue.getValue());
                    Integer value2 = Integer.parseInt(secondValue.getValue());
                    return new Value(value1 * value2);
                }

            }
            return new Value(false); 
        } else if (currentFunction.getScopedName(null).equals("Math.div")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (secondValue.skipEvaluation()) {
                    return secondValue;
                }
                if (secondValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null && secondValue.getValue() != null) {
                    Integer value1 = Integer.parseInt(firstValue.getValue());
                    Integer value2 = Integer.parseInt(secondValue.getValue());
                    return new Value(value1 / value2);
                }

            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.abs")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    Integer value1 = Integer.parseInt(firstValue.getValue());
                    return new Value(java.lang.Math.abs(value1));
                }
            }
            return new Value(false); 
        } else if (currentFunction.getScopedName(null).equals("Math.acos")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    double value1 = Double.parseDouble(firstValue.getValue());
                    
                    // convert x to radians
                    value1 = java.lang.Math.toRadians(value1);
                    return new Value(java.lang.Math.acos(value1));
                }
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.asin")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    double value1 = Double.parseDouble(firstValue.getValue());
                    
                    // convert x to radians
                    value1 = java.lang.Math.toRadians(value1);
                    return new Value(java.lang.Math.asin(value1));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.atan")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    double value1 = Double.parseDouble(firstValue.getValue());
                    
                    // convert x to radians
                    value1 = java.lang.Math.toRadians(value1);
                    return new Value(java.lang.Math.atan(value1));
                }
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.atan2")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (secondValue.skipEvaluation()) {
                    return secondValue;
                }
                if (secondValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                
                if (firstValue.getValue() != null && secondValue.getValue() != null) {
                    double value1 = Double.parseDouble(firstValue.getValue());
                    double value2 = Double.parseDouble(secondValue.getValue());
                    
                    // convert x and y to degrees
                    value1 = java.lang.Math.toDegrees(value1);
                    value2 = java.lang.Math.toDegrees(value2);

                    return new Value(java.lang.Math.atan2(value1, value2));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.cbrt")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    int value1 = Integer.parseInt(firstValue.getValue());
                    Double cbrt = java.lang.Math.cbrt(value1);
                    return new Value(cbrt.intValue());
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.cos")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    double cos = Double.parseDouble(firstValue.getValue());
                    cos = java.lang.Math.toRadians(cos);
                    return new Value(java.lang.Math.cos(cos));
                }
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.cosh")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    double cosh = Double.parseDouble(firstValue.getValue());
                    cosh = java.lang.Math.toRadians(cosh);
                    return new Value(java.lang.Math.cosh(cosh));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.exp")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null) {
                    double exp = Double.parseDouble(firstValue.getValue());
                    return new Value(java.lang.Math.exp(exp));
                }
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.hypot")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                if (firstValue.getValue() != null && secondValue != null) {
                    double hypot1 = Double.parseDouble(firstValue.getValue());
                    double hypot2 = Double.parseDouble(secondValue.getValue());
                    return new Value(java.lang.Math.hypot(hypot1, hypot2));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.log")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    return new Value(java.lang.Math.log(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.log10")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    return new Value(java.lang.Math.log10(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.pow")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null && secondValue != null) {
                    double firstAttrValue = Double.parseDouble(firstValue.getValue());
                    double secondAttrValue = Double.parseDouble(secondValue.getValue());
                    return new Value(java.lang.Math.pow(firstAttrValue, secondAttrValue));
                }
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.round")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    return new Value(java.lang.Math.round(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.signum")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    return new Value(java.lang.Math.signum(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.sin")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    
                    // convert them to radians
                    attrValue = java.lang.Math.toRadians(attrValue);
                    return new Value(java.lang.Math.sin(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.sinh")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    
                    // convert them to radians
                    attrValue = java.lang.Math.toRadians(attrValue);
                    return new Value(java.lang.Math.sinh(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.sqrt")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    return new Value(java.lang.Math.sqrt(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.tan")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    
                    // convert them in radians
                    attrValue = java.lang.Math.toRadians(attrValue);
                    return new Value(java.lang.Math.tan(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.tanh")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }

                if (firstValue.getValue() != null) {
                    double attrValue = Double.parseDouble(firstValue.getValue());
                    
                    // convert them in radians
                    attrValue = java.lang.Math.toRadians(attrValue);
                    return new Value(java.lang.Math.tanh(attrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.max")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                
                if (firstValue.getValue() != null && secondValue != null) {
                    double firstattrValue = Double.parseDouble(firstValue.getValue());
                    double secondAttrValue = Double.parseDouble(secondValue.getValue());                        
                    return new Value(java.lang.Math.max(firstattrValue, secondAttrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.min")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (firstValue.skipEvaluation()) {
                    return firstValue;
                }
                if (firstValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                
                if (firstValue.getValue() != null && secondValue != null) {
                    double firstattrValue = Double.parseDouble(firstValue.getValue());
                    double secondAttrValue = Double.parseDouble(secondValue.getValue());                        
                    return new Value(java.lang.Math.min(firstattrValue, secondAttrValue));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.avg")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value surfaceAttr = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (surfaceAttr.skipEvaluation()) {
                    return surfaceAttr;
                }
                if (surfaceAttr.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                
                if (surfaceAttr.getValue() != null ) {
                    Viewable currentClass=(Viewable) td.getElement(iomObj.getobjecttag());
                    ObjectPath attributePath = null;
                    try {
                        attributePath = validator.parseObjectOrAttributePath(currentClass, surfaceAttr.getValue());
                    } catch (Ili2cException e) {
                        EhiLogger.logError(e);
                    }
                    Value valueOfObjectPath=validator.getValueFromObjectPath(parentObject, iomObj, attributePath.getPathElements(), firstRole);
                    if (valueOfObjectPath.getValues() != null) {
                        String[] values = valueOfObjectPath.getValues();
                        double sum = 0;
                        for (String value : values) {
                            double tmpValue = Double.parseDouble(value);
                            sum += tmpValue;
                        }
                        return new Value(sum / values.length);
                    }
                }
            }
            return new Value(false);
        } else if (currentFunction.getScopedName(null).equals("Math.max2")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value surfaceAttr = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (surfaceAttr.skipEvaluation()) {
                    return surfaceAttr;
                }
                if (surfaceAttr.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                
                if (surfaceAttr.getValue() != null ) {
                    Viewable currentClass=(Viewable) td.getElement(iomObj.getobjecttag());
                    ObjectPath attributePath = null;
                    try {
                        attributePath = validator.parseObjectOrAttributePath(currentClass, surfaceAttr.getValue());
                    } catch (Ili2cException e) {
                        EhiLogger.logError(e);
                    }
                    Value valueOfObjectPath=validator.getValueFromObjectPath(parentObject, iomObj, attributePath.getPathElements(), firstRole);
                    if (valueOfObjectPath.getValues() != null) {
                        String[] values = valueOfObjectPath.getValues();
                        double maxValue = 0;
                        int index = 0;
                        for (String value : values) {
                            double tmpValue = Double.parseDouble(value);
                            if (index == 0) {
                                maxValue = tmpValue;
                            } else {
                                maxValue = java.lang.Math.max(maxValue, tmpValue);
                            }
                            index++;
                        }
                        return new Value(maxValue);
                    }
                }
            }
            return new Value(false); 
        } else if (currentFunction.getScopedName(null).equals("Math.min2")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value surfaceAttr = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (surfaceAttr.skipEvaluation()) {
                    return surfaceAttr;
                }
                if (surfaceAttr.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                
                if (surfaceAttr.getValue() != null ) {
                    Viewable currentClass=(Viewable) td.getElement(iomObj.getobjecttag());
                    ObjectPath attributePath = null;
                    try {
                        attributePath = validator.parseObjectOrAttributePath(currentClass, surfaceAttr.getValue());
                    } catch (Ili2cException e) {
                        EhiLogger.logError(e);
                    }
                    Value valueOfObjectPath=validator.getValueFromObjectPath(parentObject, iomObj, attributePath.getPathElements(), firstRole);
                    if (valueOfObjectPath.getValues() != null) {
                        String[] values = valueOfObjectPath.getValues();
                        double minValue = 0;
                        int index = 0;
                        for (String value : values) {
                            double tmpValue = Double.parseDouble(value);
                            if (index == 0) {
                                minValue = tmpValue;
                            } else {
                                minValue = java.lang.Math.min(minValue, tmpValue);
                            }
                            index++;
                        }
                        return new Value(minValue);
                    }
                }
            }
            return new Value(false);                
        } else if (currentFunction.getScopedName(null).equals("Math.sum")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value surfaceAttr = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (surfaceAttr.skipEvaluation()) {
                    return surfaceAttr;
                }
                if (surfaceAttr.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                
                if (surfaceAttr.getValue() != null ) {
                    Viewable currentClass=(Viewable) td.getElement(iomObj.getobjecttag());
                    ObjectPath attributePath = null;
                    try {
                        attributePath = validator.parseObjectOrAttributePath(currentClass, surfaceAttr.getValue());
                    } catch (Ili2cException e) {
                        EhiLogger.logError(e);
                    }
                    Value valueOfObjectPath=validator.getValueFromObjectPath(parentObject, iomObj, attributePath.getPathElements(), firstRole);
                    if (valueOfObjectPath.getValues() != null) {
                        String[] values = valueOfObjectPath.getValues();
                        double sum = 0;
                        for (String value : values) {
                            double tmpValue = Double.parseDouble(value);
                            sum += tmpValue;
                        }
                        return new Value(sum);
                    }
                }
            }
            return new Value(false);                
        } 
        return null;
    }

}
