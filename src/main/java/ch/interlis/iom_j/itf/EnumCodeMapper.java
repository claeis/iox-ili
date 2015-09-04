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
package ch.interlis.iom_j.itf;

import java.util.HashMap;
import java.util.Iterator;

import ch.interlis.ili2c.metamodel.EnumerationType;

/** Utility to map between xtf-code and itf-code of a given enumeration type.
 * @author ceis
 *
 */
public class EnumCodeMapper {
	private HashMap xtf2itfEnumTypes=new HashMap();
	/** Maps from a xtf-code to an itf-code.
	 * @param type enumeration type.
	 * @param xtfCode xtf-code
	 * @return itf-code of null if not a existing/valid xtf-code.
	 */
	public String mapXtfCode2ItfCode(EnumerationType type,String xtfCode)
	{
		HashMap mapping=null;
		if(!xtf2itfEnumTypes.containsKey(type))
		{
			java.util.ArrayList ev=new java.util.ArrayList();
			ModelUtilities.buildEnumList(ev,"",type.getConsolidatedEnumeration());
			Iterator iter=ev.iterator();
			mapping=new HashMap();
			int code=0;
			while(iter.hasNext()){
				String ili1code=Integer.toString(code);
				String ili2code=(String)iter.next();
				mapping.put(ili2code,ili1code);
				code++;
			}
			xtf2itfEnumTypes.put(type,mapping);
		}else{
			mapping=(HashMap)xtf2itfEnumTypes.get(type);
		}
		if(mapping.containsKey(xtfCode)){
			return (String)mapping.get(xtfCode);
		}
		return null;
	}	
	private HashMap itf2xtfEnumTypes=new HashMap();
	/** Maps from an itf-code to an xtf-code.
	 * @param type enumeration type.
	 * @param itfCode itf-code
	 * @return xtf-code or null if not an existing/valid itf-code
	 */
	public String mapItfCode2XtfCode(EnumerationType type,String itfCode)
	{
		HashMap mapping=null;
		if(!itf2xtfEnumTypes.containsKey(type))
		{
			java.util.ArrayList ev=new java.util.ArrayList();
			ModelUtilities.buildEnumList(ev,"",type.getConsolidatedEnumeration());
			Iterator iter=ev.iterator();
			mapping=new HashMap();
			int code=0;
			while(iter.hasNext()){
				String ili1code=Integer.toString(code);
				String ili2code=(String)iter.next();
				mapping.put(ili1code,ili2code);
				code++;
			}
			itf2xtfEnumTypes.put(type,mapping);
		}else{
			mapping=(HashMap)itf2xtfEnumTypes.get(type);
		}
		if(mapping.containsKey(itfCode)){
			return (String)mapping.get(itfCode);
		}
		return null;
	}
}
