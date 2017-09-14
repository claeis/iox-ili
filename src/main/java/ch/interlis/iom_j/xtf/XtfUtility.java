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
package ch.interlis.iom_j.xtf;

import java.util.ArrayList;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.impl.MyHandler;
import ch.interlis.iox.*;

/** Utility functions related to XTF files.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 13.02.2007 $
 */
public class XtfUtility {
	private XtfUtility(){}; // preventing instantiation 
	/** Gets the models used in an XTF file.
	 * @param xtffile path of XTF file
	 * @return list of model names (list&lt;String modelname&gt;) 
	 * @throws ch.interlis.iox.IoxException
	 */
	@Deprecated
	public static ArrayList<String> getModels(java.io.File xtffile)
		throws ch.interlis.iox.IoxException
	{
		ArrayList<String> ret=new ArrayList<String>();
		XtfReader reader=null;
		try{
			reader=new XtfReader(xtffile);
			IoxEvent event;
			while((event=reader.read())!=null){
				if(event instanceof StartBasketEvent){
					String topic=((StartBasketEvent)event).getType();
					String model[]=topic.split("\\.");
					ret.add(model[0]);
					return ret;
				}else if(event instanceof XtfStartTransferEvent){
					XtfStartTransferEvent xtfStart=(XtfStartTransferEvent)event;
					addModels(ret, xtfStart);
				}
			}
		}finally{
			if(reader!=null){
				reader.close();
			}
			reader=null;
		}
		return ret;
	}
	@Deprecated
	public static void addModels(ArrayList<String> ret, XtfStartTransferEvent xtfStart) {
		java.util.HashMap<String, IomObject> objs=xtfStart.getHeaderObjects();
		if(objs!=null){
			for(String tid:objs.keySet()){
				IomObject obj=objs.get(tid);
				if(obj.getobjecttag().equals(MyHandler.HEADER_OBJECT_MODELENTRY)){
					ret.add(obj.getattrvalue(MyHandler.HEADER_OBJECT_MODELENTRY_NAME));
				}
			}
		}
	}

}
