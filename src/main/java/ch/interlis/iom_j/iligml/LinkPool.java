package ch.interlis.iom_j.iligml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;

public class LinkPool {
	private Map<AssociationDef, LinkTable> associationMap =new HashMap<AssociationDef, LinkTable>();
	private IoxFactoryCollection factory=null;
	
	public LinkPool(){}

	public void addAssocLink(AssociationDef association, RoleDef role, String objectoid, String refOid) {
		LinkTable linkTable=null;
		ArrayList<RoleDef> roles=new ArrayList<RoleDef>();
		if(!associationMap.containsKey(association)){
			Iterator roleIter=association.getRolesIterator();
			while(roleIter.hasNext()){
				RoleDef aRole=(RoleDef) roleIter.next();
				roles.add(aRole);
			}
			linkTable=new LinkTable(roles.get(0), roles.get(1));
		}else{
			linkTable=associationMap.get(association);
		}
		linkTable.addReference(objectoid, role, refOid);
		associationMap.put(association, linkTable);
	}
	
	public Iterator<IomObject> getLinkObjects() throws IoxException{
		return new Iterator<IomObject>(){
			Iterator<AssociationDef> assocIt=associationMap.keySet().iterator();
			Iterator<Pair> pairIt=null;
			
			AssociationDef currentAssoc=null;
		    RoleDef role1=null;
		    RoleDef role2=null;
			@Override
			public boolean hasNext() {
				if(pairIt!=null && pairIt.hasNext()){
					return true;
				}
				while(assocIt.hasNext()){
					currentAssoc=assocIt.next();
				    role1=associationMap.get(currentAssoc).getRole1();
				    role2=associationMap.get(currentAssoc).getRole2();
					pairIt=associationMap.get(currentAssoc).getIterator();
					if(pairIt.hasNext()){
						return true;
					}
				}
				return false;
			}

			@Override
			public IomObject next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				Pair link=pairIt.next();
			    IomObject iomObj=new Iom_jObject(currentAssoc.getScopedName(), null);
			    iomObj.addattrobj(role1.getName(), "REF").setobjectrefoid(link.getRoleId1());
			    iomObj.addattrobj(role2.getName(), "REF").setobjectrefoid(link.getRoleId2());
				return iomObj;
			}

			@Override
			public void remove() {
				throw new java.lang.UnsupportedOperationException();
			}
		};
	}
	
	public IomObject createIomObject(String type, String oid) throws IoxException {
        return factory.createIomObject(type, oid);
    }
}