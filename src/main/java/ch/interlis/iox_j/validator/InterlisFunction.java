package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.plugins.IoxPlugin;

public interface InterlisFunction extends IoxPlugin {
    /** Name of the transient value in Settings that holds the IoxDataPool used by the Validator.
     * IoxDataPool pipelinePool=settings.getIntermediateValue(IOX_DATA_POOL);
     */
    public static final String IOX_DATA_POOL="ch.interlis.iox_j.validator.IoxDataPool"; 
    /** Name of the transient value in Settings that holds the Validator.
     * Validator validator=settings.getIntermediateValue(IOX_VALIDATOR);
     */
    public static final String IOX_VALIDATOR="ch.interlis.iox_j.validator.Validator"; 
	public void init(TransferDescription td,Settings settings,IoxValidationConfig validationConfig, ObjectPool objectPool, LogEventFactory logEventFactory);
	/** evaluate the function.
	 * @param validationKind controls how to log the optional validation message. 
	 * Possible values: ValidationConfig.OFF, ValidationConfig.ON, ValidationConfig.WARNING
	 * @param usageScope from where is the function called (e.g. the qualified constraint name).
	 * @param mainObj the actual main data object
	 * @param actualArguments  the actual argument values
	 * @return the result value of the evaluation 
	 * @see ValidationConfig.OFF, ValidationConfig.ON, ValidationConfig.WARNING
	 */
	public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments);
	public String getQualifiedIliName();
}
