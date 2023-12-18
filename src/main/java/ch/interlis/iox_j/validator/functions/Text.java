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

public class Text {
    public static final String TEXT="Text";
    public static final String TEXT_V2="Text_V2";
    
    private TransferDescription td = null;
    private ch.interlis.iox.IoxValidationConfig validationConfig = null;
    private Validator validator=null;
    
    public Text(Validator validator, TransferDescription td, ch.interlis.iox.IoxValidationConfig validationConfig) {
        this.td = td;
        this.validationConfig = validationConfig;
        this.validator=validator;
    }
    
    public Value evaluateFunction(Function currentFunction, FunctionCall functionCallObj, IomObject parentObject, 
            String validationKind, String usageScope, IomObject iomObj, TextType texttype, RoleDef firstRole) {
        
        if (currentFunction.getName().equals("compareToIgnoreCase") || 
                currentFunction.getName().equals("compareToIgnoreCaseM")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments.length == 2) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()){
                    return firstValue;
                }
                if (firstValue.isUndefined()){
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
                if (secondValue.skipEvaluation()){
                    return secondValue;
                }
                if (secondValue.isUndefined()){
                    return Value.createSkipEvaluation();
                }                    
                if(firstValue.getValue() != null && secondValue.getValue() != null){
                    return new Value(firstValue.getValue().compareToIgnoreCase(secondValue.getValue()));
                }
            }
            return new Value(false);
        } else if (currentFunction.getName().equals("concat") || 
                currentFunction.getName().equals("concatM")) { 
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments.length == 2) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()){
                    return firstValue;
                    }
                if (firstValue.isUndefined()){
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
                if (secondValue.skipEvaluation()){
                    return secondValue;
                }
                if (secondValue.isUndefined()){
                    return Value.createSkipEvaluation();
                }                    
                if(firstValue.getValue() != null && secondValue.getValue() != null){
                    return new Value(texttype, firstValue.getValue().concat(secondValue.getValue()));
                }
            }
            return new Value(false);
        } else if (currentFunction.getName().equals("endsWith") || 
                currentFunction.getName().equals("endsWithM")) { 
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments.length == 2) {
                Value firstValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (firstValue.skipEvaluation()){
                    return firstValue;
                }
                if (firstValue.isUndefined()){
                    return Value.createSkipEvaluation();
                }
                Value secondValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[1], firstRole);
                if (secondValue.skipEvaluation()){
                    return secondValue;
                    }
                if (secondValue.isUndefined()){
                    return Value.createSkipEvaluation();
                }                    
                if(firstValue.getValue() != null && secondValue.getValue() != null){
                    return new Value(firstValue.getValue().endsWith(secondValue.getValue()));
                }
            }
            return new Value(false);
        } else if (currentFunction.getName().equals("equalsIgnoreCase") || 
                currentFunction.getName().equals("equalsIgnoreCaseM")) { 
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments.length == 2) {
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
                    return new Value(firstValue.getValue().equalsIgnoreCase(secondValue.getValue()));
                }
            }
            return new Value(false);
        } else if (currentFunction.getName().equals("indexOf") || 
                currentFunction.getName().equals("indexOfM")) { 
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
                Value fromIndexValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[2], firstRole);
                if (fromIndexValue.getValue() == null) {
                    return new Value(firstValue.getValue().indexOf(secondValue.getValue()));
                } else {
                    int fromIndex = Integer.parseInt(fromIndexValue.getValue());
                    return new Value(firstValue.getValue().indexOf(secondValue.getValue(), fromIndex));
                }

            }
            return new Value(false);
        } else if (currentFunction.getName().equals("lastIndexOf") || 
                currentFunction.getName().equals("lastIndexOfM")) { 
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
                Value fromIndexValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[2], firstRole);
                if (fromIndexValue.getValue() == null) {
                    return new Value(firstValue.getValue().lastIndexOf(secondValue.getValue()));
                } else {
                    int fromIndex = Integer.parseInt(fromIndexValue.getValue());
                    return new Value(firstValue.getValue().lastIndexOf(secondValue.getValue(), fromIndex));
                }

            }
            return new Value(false);                
        } else if (currentFunction.getName().equals("matches") ||
                currentFunction.getName().equals("matchesM")) { 
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments.length == 2) {
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
                    return new Value(firstValue.getValue().matches(secondValue.getValue()));
                }
            }
            return new Value(false);
        } else if (currentFunction.getName().equals("replace") ||
                currentFunction.getName().equals("replaceM")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value mainText = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (mainText.skipEvaluation()) {
                    return mainText;
                }
                if (mainText.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value oldValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (oldValue.skipEvaluation()) {
                    return oldValue;
                }
                if (oldValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value newValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[2], firstRole);
                if (newValue.skipEvaluation()) {
                    return newValue;
                }
                if (newValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }                    
                if (mainText.getValue() != null && oldValue.getValue() != null && newValue.getValue() != null) {
                    return new Value(texttype, mainText.getValue().replace(oldValue.getValue(), newValue.getValue()));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getName().equals("startsWith") ||
                currentFunction.getName().equals("startsWithM")) {
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
                    return new Value(firstValue.getValue().startsWith(secondValue.getValue()));
                }
            }
            return new Value(false);
        } else if (currentFunction.getName().equals("substring") ||
                currentFunction.getName().equals("substringM")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            if (arguments != null) {
                Value mainText = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[0], firstRole);
                if (mainText.skipEvaluation()) {
                    return mainText;
                }
                if (mainText.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value fromValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[1], firstRole);
                if (fromValue.skipEvaluation()) {
                    return fromValue;
                }
                if (fromValue.isUndefined()) {
                    return Value.createSkipEvaluation();
                }
                Value toValue = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[2], firstRole);
              
                if (mainText.getValue() != null && toValue.isUndefined()) {
                    int fromIndex = Integer.parseInt(fromValue.getValue());
                    return new Value(texttype, mainText.getValue().substring(fromIndex));
                } else {
                    int fromIndex = Integer.parseInt(fromValue.getValue());
                    int toIndex = Integer.parseInt(toValue.getValue());
                    return new Value(texttype, mainText.getValue().substring(fromIndex, toIndex));
                }
            }
            return new Value(false);                
        } else if (currentFunction.getName().equals("toLowerCase") ||
                currentFunction.getName().equals("toLowerCaseM")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            for(Evaluable anArgument : arguments){
                Value arg = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,anArgument, firstRole);
                if (arg.skipEvaluation()){
                    return arg;
                }
                if (arg.isUndefined()){
                    return Value.createSkipEvaluation();
            }
                return new Value(texttype, arg.getValue().toLowerCase());
            }
            return new Value(false);
        } else if (currentFunction.getName().equals("toUpperCase") ||
                currentFunction.getName().equals("toUpperCaseM")) {
            Evaluable[] arguments = functionCallObj.getArguments();
            for(Evaluable anArgument : arguments){
                Value arg = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj,anArgument, firstRole);
                if (arg.skipEvaluation()){
                    return arg;
                }
                if (arg.isUndefined()){
                    return Value.createSkipEvaluation();
            }
                return new Value(texttype, arg.getValue().toUpperCase());
            }   
            return new Value(false);
        } 
        return null;
    }

}
