package ch.interlis.iox_j.validator;

import java.util.List;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.plugins.IoxPlugin;

public interface ExternalObjectResolver extends IoxPlugin {
	public void init(TransferDescription td,Settings settings,IoxValidationConfig validationConfig, ObjectPool objectPool, LogEventFactory logEventFactory);
	public boolean objectExists(String oid, List<Viewable> classCandidates);
}
