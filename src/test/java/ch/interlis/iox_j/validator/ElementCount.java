package ch.interlis.iox_j.validator;

import java.util.Collection;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.Value;

public class ElementCount implements InterlisFunction {

	private IomObject mainObj;
	private Value[] actualArguments;
	
	@Override
	public void init(TransferDescription td, FunctionCall func,Settings settings,IoxValidationConfig validationConfig) {
	}

	@Override
	public void addObject(IomObject mainObj, Value[] actualArguments) {
		this.setMainObj(mainObj);
		this.setActualArguments(actualArguments);
	}

	@Override
	public Value evaluate() {
		Value[] args = getActualArguments();
		if (args[0].skipEvaluation()){
			return args[0];
		}
		if (args[0].isUndefined()){
			return Value.createSkipEvaluation();
		}
		Collection<IomObject> obj1 = args[0].getValues();
		if(obj1==null){
			throw new IllegalArgumentException(obj1.toString());
		}
		return new Value(true);
	}

	private IomObject getMainObj() {
		return mainObj;
	}

	private void setMainObj(IomObject mainObj) {
		this.mainObj = mainObj;
	}

	private Value[] getActualArguments() {
		return actualArguments;
	}

	private void setActualArguments(Value[] actualArguments) {
		this.actualArguments = actualArguments;
	}
}