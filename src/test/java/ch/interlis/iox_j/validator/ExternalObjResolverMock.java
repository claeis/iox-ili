package ch.interlis.iox_j.validator;

import java.util.List;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;

public class ExternalObjResolverMock implements ExternalObjectResolver {

	public static String OID1 = "ExternalObjResolverMock.1";
	
	@Override
	public void init(TransferDescription td, Settings settings,
		IoxValidationConfig validationConfig, ObjectPool objectPool,
		LogEventFactory logEventFactory) {
	}

	@Override
	public boolean objectExists(String oid, List<Viewable> classCandidates) {
		if(oid.equals(OID1)){
			return true;
		}
		return false;
	}

}
