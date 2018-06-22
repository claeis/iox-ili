/* This file is part of the iox-ili project.
 * For more information, please see <http://www.eisenhutinformatik.ch/iox-ili/>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package ch.interlis.iom_j;
	
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
	/** 
	 * This class provides a default implementation for the IomObject interface.
	 * @author ceis
	 */
	public class Iom_jObject implements ch.interlis.iom.IomObject, java.io.Serializable
	{
		// HashMap<string attrname,ArrayList<string|iomObject>>
	    private final HashMap<String, ArrayList> attrv = new HashMap<String, ArrayList>();

		/** 
		 * creates a new object. Do not use this function, unless you know what you do.
		 * Applications should use the factory method of the reader or writer.
		 * @param tag1 type of new object.
		 * @param oid1 oid of new object. May be null in case of a structure.
		 */
		public Iom_jObject(String tag1, String oid1)
		{
            tag = tag1;
            oid = oid1;
		}
        public Iom_jObject(ch.interlis.iom.IomObject src)
        {
            this(src.getobjecttag(),src.getobjectoid());
            this.col=src.getobjectcol();
            this.consistency=src.getobjectconsistency();
            this.line=src.getobjectline();
            this.op=src.getobjectoperation();
            this.orderpos=src.getobjectreforderpos();
            this.refbid=src.getobjectrefbid();
            this.refoid=src.getobjectrefoid();
            int attrc=src.getattrcount();
            for(int attri=0;attri<attrc;attri++) {
                String attrName=src.getattrname(attri);
                int valuec=src.getattrvaluecount(attrName);
                for(int valuei=0;valuei<valuec;valuei++) {
                    String valueStr=src.getattrprim(attrName, valuei);
                    if(valueStr!=null) {
                        addattrvalue(attrName,valueStr);
                    }else {
                        ch.interlis.iom.IomObject valueObj=src.getattrobj(attrName, valuei);
                        addattrobj(attrName, new Iom_jObject(valueObj));
                    }
                }
            }
        }
		/** @deprecated
		 */
		@Deprecated
		@Override
		public ch.interlis.iom.IomObject addattrobj(
			String attrName,
			String type) {
				Iom_jObject ret = null;
				if (attrv.containsKey(attrName))
				{
					// found, add
					ArrayList valuev = attrv.get(attrName);
					ret = new Iom_jObject(type, null);
					valuev.add(ret);
				}
				else
				{
					// not found, add
					ArrayList valuev = new ArrayList();
					ret = new Iom_jObject(type, null);
					valuev.add(ret);
					attrv.put(attrName, valuev);
				}
				return ret;
		}
		@Override
		public void addattrobj(
			String attrName,
			ch.interlis.iom.IomObject obj) {
				if(obj==null){
					throw new IllegalArgumentException("illegal argument obj (=null)");
				}
				if (attrv.containsKey(attrName))
				{
					// found, add
					ArrayList valuev = attrv.get(attrName);
					valuev.add(obj);
				}
				else
				{
					// not found, add
					ArrayList valuev = new ArrayList();
					valuev.add(obj);
					attrv.put(attrName, valuev);
				}
		}
        private void addattrvalue(
            String attrName,
            String attrValue) {
                if(attrValue==null){
                    throw new IllegalArgumentException("illegal argument obj (=null)");
                }
                if (attrv.containsKey(attrName))
                {
                    // found, add
                    ArrayList valuev = attrv.get(attrName);
                    valuev.add(attrValue);
                }
                else
                {
                    // not found, add
                    ArrayList valuev = new ArrayList();
                    valuev.add(attrValue);
                    attrv.put(attrName, valuev);
                }
        }

		/** @deprecated
		 */
		@Deprecated
		@Override
		public ch.interlis.iom.IomObject changeattrobj(
			String attrName,
			int index,
			String type) {
				Iom_jObject ret = null;
				if (attrv.containsKey(attrName))
				{
					// found, change
					ArrayList valuev = attrv.get(attrName);
					ret = new Iom_jObject(type, null);
					valuev.set(index,ret);
				}
				else
				{
					// not found, add
					ArrayList valuev = new ArrayList();
					ret = new Iom_jObject(type, null);
					valuev.set(index,ret);
					attrv.put(attrName, valuev);
				}
				return ret;
		}
		@Override
		public void changeattrobj(
			String attrName,
			int index,
			ch.interlis.iom.IomObject obj) {
			if(obj==null){
				throw new IllegalArgumentException("illegal argument obj (=null)");
			}
				if (attrv.containsKey(attrName))
				{
					// found, change
					ArrayList valuev = attrv.get(attrName);
					valuev.set(index,obj);
				}
				else
				{
					// not found, add
					ArrayList valuev = new ArrayList();
					valuev.set(index,obj);
					attrv.put(attrName, valuev);
				}
		}

		@Override
		public void delete() {
		}

		@Override
		public void deleteattrobj(String attrName, int index) {
			if (attrv.containsKey(attrName))
			{
				// found, remove
				ArrayList valuev = attrv.get(attrName);
				if (index>=0 && index<valuev.size())
				{
					valuev.remove(index);
					if(valuev.size()==0){
						attrv.remove(attrName);
					}
				}
			}
			else
			{
				// not found, nothing to do
			}
		}

		@Override
		public int getattrcount() {
			return attrv.size();
		}

		@Override
		public String getattrname(int index) {
			int i=0;
			for(Iterator attri=attrv.keySet().iterator();attri.hasNext();)
			{
				String attr=(String)attri.next();
				if (i == index)
				{
					return attr;
				}
				i++;
			}
			return null;
		}

		@Override
		public ch.interlis.iom.IomObject getattrobj(
			String attrName,
			int index) {
				if (attrv.containsKey(attrName))
				{
					ArrayList valuev = attrv.get(attrName);
					Object val = valuev.get(index);
					if (val instanceof Iom_jObject)
					{
						return (Iom_jObject)val;
					}
				}
				return null;
		}

		@Override
		public String getattrprim(String attrName, int index) 
		{
			if (attrv.containsKey(attrName))
			{
				ArrayList valuev = attrv.get(attrName);
				Object val=valuev.get(index);
				if(val instanceof String){
					return (String)val;
				}
			}
			return null;
		}

		@Override
		public String getattrvalue(String attrName) {
			if (attrv.containsKey(attrName))
			{
				ArrayList valuev = attrv.get(attrName);
				Object val = valuev.get(0);
				if (val instanceof String)
				{
					return (String)val;
				}
			}
			return null;
		}

		@Override
		public int getattrvaluecount(String attrName) {
			if (attrv.containsKey(attrName))
			{
				ArrayList valuev = attrv.get(attrName);
				return valuev.size();
			}
			return 0;
		}
		private int col=0;
		@Override
		public int getobjectcol() {
			return col;
		}
		private int line=0;
		@Override
		public int getobjectline() {
			return line;
		}
		@Override
		public void setobjectcol(int col) {
			this.col=col;
		}

		@Override
		public void setobjectline(int line) {
			this.line=line;
		}
		@Override
		public String getxmleleattrname(int index) {
			return null;
		}

		@Override
		public int getxmlelecount() {
			return 0;
		}

		@Override
		public ch.interlis.iom.IomObject getxmleleobj(int index) {
			return null;
		}

		@Override
		public String getxmleleprim(int index) {
			return null;
		}

		@Override
		public int getxmlelevalueidx(int index) {
			return 0;
		}

		/** @deprecated
		 */
		@Deprecated
		@Override
		public ch.interlis.iom.IomObject insertattrobj(
			String attrName,
			int index,
			String type) {
				Iom_jObject ret = null;
				if (attrv.containsKey(attrName))
				{
					// found, insert
					ArrayList valuev = attrv.get(attrName);
					ret = new Iom_jObject(type, null);
					valuev.add(index,ret);
				}
				else
				{
					// not found, add
					ArrayList valuev = new ArrayList();
					ret = new Iom_jObject(type, null);
					valuev.add(index,ret);
					attrv.put(attrName, valuev);
				}
				return ret;
		}
		@Override
		public void insertattrobj(
			String attrName,
			int index,
			ch.interlis.iom.IomObject obj) {
			if(obj==null){
				throw new IllegalArgumentException("illegal argument obj (=null)");
			}
				if (attrv.containsKey(attrName))
				{
					// found, insert
					ArrayList valuev = attrv.get(attrName);
					valuev.add(index,obj);
				}
				else
				{
					// not found, add
					ArrayList valuev = new ArrayList();
					valuev.add(index,obj);
					attrv.put(attrName, valuev);
				}
		}

		@Override
		public void setattrundefined(String attrName) {
			attrv.remove(attrName);
		}

		@Override
		public void setattrvalue(String attrName, String value) 
		{
		    if(value==null) {
	              throw new IllegalArgumentException("illegal value null for "+attrName);
		    }else {
	            if (attrv.containsKey(attrName))
	            {
	                ArrayList valuev = attrv.get(attrName);
	                valuev.clear();
	                valuev.add(value);
	            }
	            else
	            {
	                ArrayList valuev = new ArrayList();
	                valuev.add(value);
	                attrv.put(attrName, valuev);
	            }
		    }
		}

		private int consistency;
		@Override
		public int getobjectconsistency() {
			return consistency;
		}
		@Override
		public void setobjectconsistency(int consistency1) {
			consistency=consistency1;
		}

		private String oid;
		@Override
		public String getobjectoid() {
			return oid;
		}
		@Override
		public void setobjectoid(String oid1) {
			oid=oid1;
		}

		private int op;
		@Override
		public int getobjectoperation() {
			return op;
		}
		@Override
		public void setobjectoperation(int operation) {
			op=operation;
		}

		private String refbid;
		@Override
		public String getobjectrefbid() {
			return refbid;
		}
		@Override
		public void setobjectrefbid(String refbid1) {
			refbid=refbid1;
		}

		private String refoid;
		@Override
		public String getobjectrefoid() {
			return refoid;
		}
		@Override
		public void setobjectrefoid(String refoid1) {
			refoid=refoid1;
		}

		private long orderpos;
		@Override
		public long getobjectreforderpos() {
			return orderpos;
		}
		@Override
		public void setobjectreforderpos(long orderpos1) {
			orderpos=orderpos1;
		}

		private String tag;
		@Override
		public String getobjecttag() {
			return tag;
		}
		@Override
		public void setobjecttag(String tag1) {
			tag=tag1;
		}

		/** 
		 * Returns a string representation of the object. Same as dumpObject().
		 */
		@Override
		public String toString() {
			return dumpObject(this);
		}
		/** 
		 * converts the contents of the given object for debuggung to a one line String.
		 * @param obj object to convert
		 * @return object as a String
		 */
		public static String dumpObject(ch.interlis.iom.IomObject obj)
		{
			StringBuffer ret=new StringBuffer();
			int i;
			ch.interlis.iom.IomObject structvalue;
			String value;
			String refoid;
			long orderPos;
			String className=obj.getobjecttag();
			String oid=obj.getobjectoid();
			ret.append(className);
			if(oid!=null){
				ret.append(" oid "+oid);
			}
			ret.append(" {");
			String sep="";
			int attrc = obj.getattrcount();
			String propNames[]=new String[attrc];
			for(i=0;i<attrc;i++){
				   propNames[i]=obj.getattrname(i);
			}
			java.util.Arrays.sort(propNames);
			for(i=0;i<attrc;i++){
			   String propName=propNames[i];
				int propc=obj.getattrvaluecount(propName);
				if(propc>0){
					ret.append(sep+propName);
					String sep2=" ";
					if(propc>1){
						ret.append(" [");
						sep2="";
					}
					for(int propi=0;propi<propc;propi++){
					    value=obj.getattrprim(propName,propi);
					    if(value!=null){
							ret.append(sep2+value);
					    }else{
							structvalue=obj.getattrobj(propName,propi);
							refoid=structvalue.getobjectrefoid();
							if(refoid!=null){
								orderPos=structvalue.getobjectreforderpos();
								if(orderPos!=0){
									sep2=sep2+"-> "+refoid+", ORDER_POS "+orderPos+" ";
								}else{
									sep2=sep2+"-> "+refoid+" ";
								}
							}
							ret.append(sep2+dumpObject(structvalue));
					    }
						sep2=", ";
					}
					if(propc>1){
						ret.append("]");
					}
					sep=", ";
				}
			}
			ret.append("}");
			return ret.toString();
		}
}
