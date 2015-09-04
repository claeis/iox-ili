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
package ch.interlis.iox_j;

/** first IoxEvent read from a IoxReader or written to a IoxWriter.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 26.06.2006 $
 */
public class StartTransferEvent implements ch.interlis.iox.StartTransferEvent {
	private String sender=null;
	private String comment=null;
	private String version=null;
	public StartTransferEvent(){
	}
	public StartTransferEvent(String sender){
		this.sender=sender;
	}
	public StartTransferEvent(String sender,String comment){
		this.sender=sender;
		this.comment=comment;
	}
	public StartTransferEvent(String sender,String comment,String version){
		this.sender=sender;
		this.comment=comment;
		this.version=version;
	}
	public String getComment() {
		return comment;
	}
	public String getSender() {
		return sender;
	}
	/** Sets the comment about this transfer file.
	 */
	public void setComment(String string) {
		comment = string;
	}
	/** Sets the sender of this transfer file.
	 */
	public void setSender(String string) {
		sender = string;
	}
	public String getVersion() {
		return version;
	}
	/** Sets the transfer format version of this transfer file.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
