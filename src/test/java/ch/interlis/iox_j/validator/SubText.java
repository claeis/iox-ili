package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.Value;

public class SubText implements InterlisFunction {

	
	@Override
	public void init(TransferDescription td,Settings settings,IoxValidationConfig validationConfig, ObjectPool objectPool, LogEventFactory logEventFactory) {
	}

	@Override
	public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] args) {
		if (args[0].skipEvaluation()){
			return args[0];
		}
		if (args[0].isUndefined()){
			return Value.createSkipEvaluation();
		}
		String textValue = args[0].getValue();
		if (args[1].skipEvaluation()){
			return args[1];
		}
		if (args[1].isUndefined()){
			return Value.createSkipEvaluation();
		}
		int beginIndex = Integer.parseInt(args[1].getValue());
		if (args[2].skipEvaluation()){
			return args[2];
		}
		if (args[2].isUndefined()){
			return Value.createSkipEvaluation();
		}
		int endIndex = Integer.parseInt(args[2].getValue());
		String subStringValue = textValue.substring(beginIndex, endIndex);
		TextType text = new TextType();
		return new Value(text, subStringValue);
	}


	@Override
	public String getQualifiedIliName() {
		
		return "FunctionsExt23.subText";
	}
}