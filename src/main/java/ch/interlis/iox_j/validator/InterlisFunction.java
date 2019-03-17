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
	public void init(TransferDescription td,Settings settings,IoxValidationConfig validationConfig, ObjectPool objectPool, LogEventFactory logEventFactory);
	public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments);
	public String getQualifiedIliName();
}
