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
package ch.interlis.iom_j.iligml;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.ArrayList;

import ch.interlis.iox.IoxException;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.PredefinedModel;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.Ili2cUtility;


/** This class implements an INTERLIS 2 writer using the ili2c metamodel.
 * @author ceis
 */
public class Etf1Writer extends Etf1WriterBase {

	/** Creates a new instance of a writer.
	 * @param buffer Output writer to write to
	 * @param td models as read by the compiler (ili2c)
	 * @throws IoxException
	 */
	public Etf1Writer(OutputStreamWriter buffer, TransferDescription td) throws IoxException {
		super(buffer, Ili2cUtility.getIoxMappingTable(td)
				,ch.interlis.ili2c.generator.Gml32Generator.createName2NameMapping(td)
				,td.getLastModel().getIliVersion());
		setModels(buildModelList(td));
	}

	/** Creates a new instance of a writer.
	 * @param outfile File to write to
	 * @param td models as read by the compiler (ili2c)
	 * @throws IoxException
	 */
	public Etf1Writer(File outfile, TransferDescription td)
			throws IoxException {
		super(outfile, Ili2cUtility.getIoxMappingTable(td)
				,ch.interlis.ili2c.generator.Gml32Generator.createName2NameMapping(td)
				, td.getLastModel().getIliVersion());
		setModels(buildModelList(td));
	}

	/** Creates a new instance of a writer.
	 * @param buffer Output stream to write to
	 * @param td models as read by the compiler (ili2c)
	 * @throws IoxException
	 */
	public Etf1Writer(OutputStream buffer, TransferDescription td) throws IoxException {
		super(buffer, Ili2cUtility.getIoxMappingTable(td)
				,ch.interlis.ili2c.generator.Gml32Generator.createName2NameMapping(td)
				, td.getLastModel().getIliVersion());
		setModels(buildModelList(td));
	}
	private XtfModel[] buildModelList(TransferDescription td){
		ArrayList modelv=new ArrayList();
		Iterator modeli=td.iterator();
		while(modeli.hasNext()){
			Object modelo=modeli.next();
			if(modelo instanceof PredefinedModel){
				continue;
			}
			if(modelo instanceof Model){
				modelv.add(modelo);
			}
		}
		XtfModel[] ret=new XtfModel[modelv.size()];
		for(int i=0;i<modelv.size();i++){
			Model model=(Model)modelv.get(i);
			ret[i]=new XtfModel();
			ret[i].setName(model.getName());
			String version=model.getModelVersion();
			ret[i].setVersion(version==null?"":version);
			String issuer=model.getIssuer();
			ret[i].setUri(issuer==null?"":issuer);
		}
		return ret;
	}

}
