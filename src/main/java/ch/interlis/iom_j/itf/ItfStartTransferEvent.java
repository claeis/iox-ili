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

import ch.interlis.iox_j.StartTransferEvent;

/** This class holds additional header information of an INTERLIS 1 transfer file.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 26.06.2006 $
 */
public class ItfStartTransferEvent extends StartTransferEvent {
	private String modelId=null;
	private String modelDefinition=null;
	/** Gets the model (schema), if it is included in the transfer file.
	 * @return model in INTERLIS syntax or null
	 */
	public String getModelDefinition() {
		return modelDefinition;
	}
	/** Gets the model id, if available.
	 * @return model id (ususally filename of model) or null
	 */
	public String getModelId() {
		return modelId;
	}
	/** Sets the model.
	 * @param string Model in INTERLIS syntax
	 */
	public void setModelDefinition(String string) {
		modelDefinition = string;
	}
	/** Sets the model id.
	 * @param string model id (usually filename of model)
	 */
	public void setModelId(String string) {
		modelId = string;
	}
}
