package ch.interlis.iox_j.db;


/**
 * @author ce
 * @version $Revision: 1.0 $ $Date: 02.06.2006 $
 */
public class IdentityEnumMapper implements EnumMapper {

	public String mapToIliCode(String recInfo,String dbVal) {
		return dbVal;
	}

}
