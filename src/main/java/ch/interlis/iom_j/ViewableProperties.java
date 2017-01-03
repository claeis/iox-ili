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

import java.util.*;

/** This class implements a lightweight representation of a model.
 * @author ceis
 */
public class ViewableProperties
{
    private HashMap classv = new HashMap(); // map<String className,ViewableProperty[] propertyName>
    private HashMap<String,String> xtf24nameMapping=null; 
    public HashMap<String, String> getXtf24nameMapping() {
		return xtf24nameMapping;
	}
	public void setXtf24nameMapping(HashMap<String, String> xtf24nameMapping) {
		this.xtf24nameMapping = xtf24nameMapping;
	}
	/** gets the list of properties of an interlis class or assocition.
     * @param iliClsName qualified Interlis Name of class or association
     * @return list of attributes and embedded roles
     */
    public String[] getClassProperties(String iliClsName)
    {
    	ViewableProperty[] props=getClassVProperties(iliClsName);
    	String ret[]=new String[props.length];
    	for(int i=0;i<props.length;i++){
    		ret[i]=props[i].getName();
    	}
        return ret;
    }
    /** gets the list of properties of an interlis class or assocition.
     * @param iliClsName qualified Interlis Name of class or association
     * @return list of attributes and embedded roles
     */
    public ViewableProperty[] getClassVProperties(String iliClsName)
    {
        if (!classv.containsKey(iliClsName))
        {
            throw new IllegalArgumentException("unknown class " + iliClsName);
        }
        return (ViewableProperty[])classv.get(iliClsName);
    }
    /** gets the description of a property.
     * @param iliClsName qualified Interlis Name of class or association
     * @param propName name of property
     * @return description of property or null if property doesn't exist
     */
    public ViewableProperty getClassProperty(String iliClsName,String propName)
    {
    	ViewableProperty[] props=getClassVProperties(iliClsName);
    	for(int i=0;i<props.length;i++){
    		if(props[i].getName().equals(propName)){
    			return props[i];
    		}
    	}
        return null;
    }
    /** adds a description of a class to this model.
     * @param clsName  qualified Interlis Name of class or association
     * @param propNames list of properties.
     */
    public void defineClass(String clsName,String[] propNames)
    {
    	ViewableProperty properties[]=new ViewableProperty[propNames.length];
    	for(int i=0;i<propNames.length;i++){
    		properties[i]=new ViewableProperty(propNames[i]);
    	}
        defineClass(clsName, properties);
    }
    /** adds a description of a class to this model.
     * @param clsName qualified Interlis name of class or association
     * @param properties list of properties
     */
    public void defineClass(String clsName,ViewableProperty[] properties)
    {
        classv.put(clsName, properties);
    }
    /** tests if a given class is known.
     * @param clsName qualified name of Interlis class or association
     * @return true if class exists, false otherwise
     */
    public boolean existsClass(String clsName){
        return classv.containsKey(clsName);
    }
    /** merges the descriptions of another model to this one.
     * @param other the other model to be merged with this one
     */
    public void addAll(ViewableProperties other){
    	classv.putAll(other.classv);
    	xtf24nameMapping.putAll(other.getXtf24nameMapping());
    }
    static void Main(String[] args)
	{
	    ViewableProperties mapping = new ViewableProperties();
	            mapping.defineClass("ce",new String[]{"a","b"});
	}
}
