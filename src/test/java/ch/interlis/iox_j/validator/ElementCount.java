package ch.interlis.iox_j.validator;

import java.util.Collection;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.Value;

public class ElementCount implements InterlisFunction {
	
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
		Collection<IomObject> obj1 = args[0].getComplexObjects();
		if(obj1==null){
			throw new IllegalArgumentException(obj1.toString());
		}
		return new Value(true);
	}

	@Override
	public String getQualifiedIliName() {
		return "FunctionsExt23.elementCount";
	}

}