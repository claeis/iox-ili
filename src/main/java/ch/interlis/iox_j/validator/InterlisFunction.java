package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;

public interface InterlisFunction {
	public void init(TransferDescription td, FunctionCall func,Settings settings,IoxValidationConfig validationConfig);
	public void addObject(IomObject mainObj, Value[] actualArguments);
	public Value evaluate();
}
