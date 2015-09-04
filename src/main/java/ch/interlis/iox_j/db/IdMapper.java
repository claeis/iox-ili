package ch.interlis.iox_j.db;

/**
 * @author ce
 * @version $Revision: 1.0 $ $Date: 02.06.2006 $
 */
public interface IdMapper {
	public String mapId(String idSpace,String id);
	public String newId();
}
