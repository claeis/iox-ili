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

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.ViewableProperty;
import ch.interlis.iom_j.itf.ModelUtilities;
import ch.interlis.iox.IoxException;
import ch.interlis.ili2c.generator.XSD24Generator;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;

/** This class implements an INTERLIS 2 writer using the ili2c metamodel.
 * @author ceis
 */
public class XtfWriter extends XtfWriterBase {

	/** Creates a new instance of a writer.
	 * @param buffer Output writer to write to
	 * @param td models as read by the compiler (ili2c)
	 * @throws IoxException
	 */
	public XtfWriter(OutputStreamWriter buffer, TransferDescription td) throws IoxException {
		super(buffer, Ili2cUtility.getIoxMappingTable(td), td.getLastModel().getIliVersion());
		setModels(Ili2cUtility.buildModelList(td));
	}

	/** Creates a new instance of a writer.
	 * @param outfile File to write to
	 * @param td models as read by the compiler (ili2c)
	 * @throws IoxException
	 */
	public XtfWriter(File outfile, TransferDescription td)
			throws IoxException {
		super(outfile, Ili2cUtility.getIoxMappingTable(td), td.getLastModel().getIliVersion());
		setModels(Ili2cUtility.buildModelList(td));
	}

	/** Creates a new instance of a writer.
	 * @param buffer Output stream to write to
	 * @param td models as read by the compiler (ili2c)
	 * @throws IoxException
	 */
	public XtfWriter(OutputStream buffer, TransferDescription td) throws IoxException {
		super(buffer, Ili2cUtility.getIoxMappingTable(td), td.getLastModel().getIliVersion());
		setModels(Ili2cUtility.buildModelList(td));
	}
    public static String domainsToString(Map<String, String> genericDomains) {
        String domains;
        ArrayList<String> genericNames=new ArrayList<String>(genericDomains.keySet());
        Collections.sort(genericNames);
        StringBuffer domainsBuf=new StringBuffer();
        String sep="";
        for(String genericName:genericNames) {
            String concreteName=genericDomains.get(genericName);
            domainsBuf.append(sep);sep=" ";
            domainsBuf.append(genericName);
            domainsBuf.append("=");
            domainsBuf.append(concreteName);
        }
        domains=domainsBuf.toString();
        return domains;
    }

}
