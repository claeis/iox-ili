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

import ch.interlis.iom.IomConstants;

/** start of a basket.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 26.06.2006 $
 */
public class StartBasketEvent implements ch.interlis.iox.StartBasketEvent {
	private String type=null;
	private String bid=null;
	private int consistency=IomConstants.IOM_COMPLETE;
	private int kind=IomConstants.IOM_FULL;
	private String startstate=null;
	private String endstate=null; 
	private String[] topicv=null;
	private java.util.Map<String,String> domains=new java.util.HashMap<String,String>();
	/** Creates a new start basket event.
	 * @param type Type of basket
	 * @param bid Identifier of basket
	 */
	public StartBasketEvent(String type,String bid){
		this.type=type;
		this.bid=bid;
	}
    public StartBasketEvent(String type,String bid,java.util.Map<String,String> genericDomains){
        this.type=type;
        this.bid=bid;
        this.domains=new java.util.HashMap<String,String>(genericDomains);
    }
	public String getBid() {
		return bid;
	}
	public int getConsistency() {
		return consistency;
	}
	public String getEndstate() {
		return endstate;
	}
	public int getKind() {
		return kind;
	}
	public String getStartstate() {
		return startstate;
	}
	public String[] getTopicv() {
		return topicv;
	}
	public String getType() {
		return type;
	}
	/** Sets the identifier of the basket.
	 * @param string basket identifier
	 */
	public void setBid(String string) {
		bid = string;
	}
	/** Sets the consistency of the basket.
	 */
	public void setConsistency(int i) {
		consistency = i;
	}
	/** Sets the end state of the basket.
	 */
	public void setEndstate(String string) {
		endstate = string;
	}
	/** Sets the transfer mode of the basket.
	 */
	public void setKind(int i) {
		kind = i;
	}
	/** Sets the start state of the basket.
	 */
	public void setStartstate(String string) {
		startstate = string;
	}
	/** Sets the sub-types of this basket's type, if it contains polymorphic content.
	 */
	public void setTopicv(String[] strings) {
		topicv = strings;
	}
	/** Sets the type of the basket.
	 */
	public void setType(String string) {
		type = string;
	}
	public java.util.Map<String,String> getDomains() {
		return domains;
	}
	public void addDomain(String genericDomain,String concreteDomain) {
		domains.put(genericDomain, concreteDomain);
	}
	public String getDomain(String genericDomain) {
		return domains.get(genericDomain);
	}
}
