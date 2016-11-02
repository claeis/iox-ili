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

/** This class implements a lightweight description of a property.
 * @author ceis
 */
public class ViewableProperty {
	/** Constructs a new description of a property with the given name.
	 * @param propertyName name of property
	 */
	public ViewableProperty(String propertyName)
	{
		name=propertyName;
	}
	private String name=null;
	/** Gets the name of the property.
	 * @return name of property
	 */
	public String getName() {
		return name;
	}
	/** Sets the name of the property.
	 * @param name new name of property
	 */
	public void setName(String name) {
		this.name = name;
	}
	private boolean typeOid=false;
	/** Tests if this property is of type OID.
	 * @return true if this is property is of type OID, otherwise false.
	 */
	public boolean isTypeOid() {
		return typeOid;
	}
	/** Sets/clears if this properties is of type OID.
	 * @param typeOid true if this is a OID property. 
	 * false if this property is not of type OID.
	 */
	public void setTypeOid(boolean typeOid) {
		this.typeOid = typeOid;
	}
	private boolean typeBlackboxXml=false;
	private boolean typeBlackboxBin=false;
	public boolean isTypeBlackboxXml() {
		return typeBlackboxXml;
	}
	public void setTypeBlackboxXml(boolean typeBlackboxXml) {
		this.typeBlackboxXml = typeBlackboxXml;
	}
	public boolean isTypeBlackboxBin() {
		return typeBlackboxBin;
	}
	public void setTypeBlackboxBin(boolean typeBlackboxBin) {
		this.typeBlackboxBin = typeBlackboxBin;
	}
	private String enumType=null;
	/** Gets the qualified ili-Name of the Enumeration type of this property.
	 * @return qualified ili-name or null if not a enum type property
	 */
	public String getEnumType() {
		return enumType;
	}
	/** Sets the qualified ili-Name of the Enumeration type of this property. Set to null
	 * if this is not an enumeration. 
	 */
	public void setEnumType(String gmlCodeSpace) {
		this.enumType = gmlCodeSpace;
	}
	
	private String baseDefInClass=null;
	/** gets the qualified ili-name of the class that contains the base definition of this property.
	 * null if this is not an extended property.
	 */
	public String getBaseDefInClass() {
		return baseDefInClass;
	}
	/** sets the qualified ili-name of the class that contains the base definition of this property.
	 */
	public void setBaseDefInClass(String baseDefInClass) {
		this.baseDefInClass = baseDefInClass;
	}
	private boolean typeFinal=false;
	/** tests if type of property is final.
	 */
	public boolean isTypeFinal() {
		return typeFinal;
	}
	/** sets type of property as final.
	 */
	public void setTypeFinal(boolean typeFinal) {
		this.typeFinal = typeFinal;
	}
	
}
