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
package ch.interlis.iom_j.xtf.impl;

import java.util.ArrayList;
import java.util.Stack;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import javax.xml.namespace.QName;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.OidSpace;
import ch.interlis.iom_j.xtf.XtfStartTransferEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox_j.*;

/** Raw parser of XML events. Originally written against the SAX interface.
 * @author ceis
 */
public class MyHandler
  {
	public static final String HEADER_OBJECT_MODELENTRY="iom04.metamodel.ModelEntry";
	public static final String HEADER_OBJECT_MODELENTRY_NAME="model";
	public static final String HEADER_OBJECT_MODELENTRY_URI="uri";
    public static final String HEADER_OBJECT_MODELENTRY_VERSION="version";
	
    private java.util.Set<String> filterTopics=null;
	private java.util.HashMap<String, IomObject> header=null;
	private boolean ili22=false;
	public boolean stopParser=false;
	public ch.interlis.iox.IoxEvent returnObject=null;
	class Element {
		public IomObject object=null;
		public String propertyName=null;
		public String oid=null;
		public String bid=null;
	}
    
	//StartBasketEvent dataContainer = null;
	IomObject object=null;
	java.util.ArrayList<Element> objStack=new java.util.ArrayList<Element>();
	private int BEFORE_TRANSFER=1;
	private int BEFORE_DATASECTION=2;
	private int BEFORE_HEADERSECTION=3;
	private int BEFORE_BASKET=4;
	private int BEFORE_OBJECT=5;
	// CoordValue
	private int CV_COORD=20;
	private int CV_C1=21;
	private int CV_AFTER_C1=22;
	private int CV_C2=23;
	private int CV_AFTER_C2=24;
	private int CV_C3=25;
	private int CV_AFTER_C3=26;
	// PolylineValue
	private int PV_POLYLINE=40;
	private int PV_LINEATTR=41;
	private int PV_AFTER_LINEATTRSTRUCT=42;
	private int PV_AFTER_LINEATTR=43;
	private int PV_CLIPPED=44;
	private int PV_AFTER_CLIPPED=45;
	// SegmentSequence
	private int SS_AFTER_COORD=60;
	// SurfaceValue
	private int SV_SURFACE=80;
	private int SV_CLIPPED=81;
	private int SV_AFTER_CLIPPED=82;
	// Boundaries
	private int BD_BOUNDARY=100;
	private int BD_AFTER_POLYLINE=101;
	private int BD_AFTER_BOUNDARY=102;
	// BlackboxValue
	private int BB_BINBLBOX=110;
	private int BB_XMLBLBOX=111;
	
	// StructValue
	private int ST_BEFORE_PROPERTY=125;
	private int ST_AFTER_STRUCTVALUE=126;
	private int ST_BEFORE_EMBASSOC=127;
	private int ST_BEFORE_CHARACTERS=128;
	private int ST_AFTER_COORD=129;
	private int ST_AFTER_POLYLINE=130;
	private int ST_AFTER_SURFACE=131;
	private int ST_AFTER_OID=132;
	private int ST_AFTER_BINBLBOX=133;
	private int ST_AFTER_XMLBLBOX=134;
	// HeaderSection
	private int HS_HEADERSECTION=1000;
	private int HS_MODELS=1010;
	private int HS_MODEL=1011;
	private int HS_AFTER_MODELS=1012;
	private int HS_ALIAS=1020;
	private int HS_AFTER_ALIAS=1021;
	private int HS_ENTRIES=1022;
	private int HS_TAGENTRY=1023;
	private int HS_VALENTRY=1024;
	private int HS_DELENTRY=1025;
	private int HS_OIDSPACES=1030;
	private int HS_OIDSPACE=1031;
	private int HS_AFTER_OIDSPACES=1032;
	private int HS_COMMENT=1040;
	private int HS_AFTER_COMMENT=1041;
	
	
	private StringBuilder propertyValue=null;
	private XMLEventWriter propertyXml=null;
	private java.io.StringWriter propertyXmlString=null;
	private XMLOutputFactory xmlOf=XMLOutputFactory.newFactory();
	private XMLEventFactory eventOf=XMLEventFactory.newFactory();
	private int state=BEFORE_TRANSFER;
	private int skip=0;
	private int collectXml=0;
	/** current element level
	 */
	private int level=0;
	
	private IomObject hsAliasEntries=null;
	private IomObject hsOidSpaces=null;
	private int hsOid=0;
	private String hsNextOid(){
		hsOid++;
		return Integer.toString(hsOid);
	}
	private java.util.Stack stateStack=new java.util.Stack();
	private void pushReturnState(int returnState){
		stateStack.push(new Integer(returnState));
	}
	private void popReturnState(){
		state=((Integer)stateStack.pop()).intValue();
	}
	private void changeReturnState(int returnState){
		stateStack.pop();
		stateStack.push(new Integer(returnState));
	}
	
	public void startElement(StartElement event) 
	throws IoxException
    {
    	level++;
    	if(skip>0){
    		skip++;
    		return;
    	}
    	if(collectXml>0){
    		collectXml++;
    		try {
				propertyXml.add(event);
			} catch (XMLStreamException e) {
				throw new IoxException(e);
			}
    		return;
    	}
    	String name=event.getName().getLocalPart();
    	if(state==BEFORE_TRANSFER) {
    		if(name.equals("TRANSFER")){
	    		state=BEFORE_HEADERSECTION;
	    		ili22=event.getName().getNamespaceURI().equals(XtfWriterAlt.ili22Ns);
	    		returnObject=new XtfStartTransferEvent();
	    		if(ili22){
	    			((StartTransferEvent)returnObject).setVersion("2.2");
	    		}else{
	    			((StartTransferEvent)returnObject).setVersion("2.3");
	    		}
	    		return;
    		}else {
    			throw new IoxException("Expected TRANSFER, but "+name+" found.");
    		}
    	}
		if(state==BEFORE_HEADERSECTION) {
			if(name.equals("HEADERSECTION")){
				state=HS_HEADERSECTION;
				// SENDER
				Attribute sender = event.getAttributeByName(new QName("SENDER"));
				if (sender == null){
					// SENDER is mandatory
				  throw new IllegalArgumentException ("Attribute SENDER missing in HEADERSECTION");
				}
				((StartTransferEvent)returnObject).setSender(sender.getValue());
				header=new java.util.HashMap();
				((XtfStartTransferEvent)returnObject).setHeaderObjects(header);
				return;
			}else {
				throw new IoxException("Expected HEADERSECTION, but "+name+" found.");
			}
		}
    	if(state==BEFORE_DATASECTION && name.equals("DATASECTION")){
    		state=BEFORE_BASKET;
    		return;
    	}
    	/* HEADERSECTION
    	 * 
    	 */
    	if(state==HS_HEADERSECTION && name.equals("MODELS")){
    		state=HS_MODELS;
    		return;
    	}
		if((state==HS_HEADERSECTION || state==HS_AFTER_MODELS)&& name.equals("ALIAS")){
			state=HS_ALIAS;
			return;
		}
		if(state==HS_MODELS && name.equals("MODEL")){
			state=HS_MODEL;
			// NAME
			Attribute modelName = event.getAttributeByName(new QName("NAME"));
			if (modelName == null){
				// MODEL is mandatory
			  throw new IllegalArgumentException ("Attribute NAME missing in MODEL");
			}
			// [URI]
			Attribute modelUri = event.getAttributeByName(new QName("URI"));
            Attribute modelVersion = event.getAttributeByName(new QName("VERSION"));
			IomObject model=newIomObject(HEADER_OBJECT_MODELENTRY,hsNextOid());
			parser_addAttrValue(model,HEADER_OBJECT_MODELENTRY_NAME,modelName.getValue());
			if(modelUri!=null){
				parser_addAttrValue(model,HEADER_OBJECT_MODELENTRY_URI,modelUri.getValue());
			}
            if(modelVersion!=null){
                parser_addAttrValue(model,HEADER_OBJECT_MODELENTRY_VERSION,modelVersion.getValue());
            }
			header.put(model.getobjectoid(),model);
			return;
		}
		if(state==HS_ALIAS && name.equals("ENTRIES")){
			state=HS_ENTRIES;
			// FOR
			Attribute modelName = event.getAttributeByName(new QName("FOR"));
			if (modelName == null){
				// FOR is mandatory
			  throw new IllegalArgumentException ("Attribute FOR missing in ENTRIES");
			}
			hsAliasEntries=newIomObject("iom04.metamodel.AliasEntries",hsNextOid());
			parser_addAttrValue(hsAliasEntries,"forModel",modelName.getValue());
			header.put(hsAliasEntries.getobjectoid(),hsAliasEntries);
			return;
		}
		if(state==HS_ENTRIES && name.equals("TAGENTRY")){
			state=HS_TAGENTRY;
			// FROM
			Attribute from = event.getAttributeByName(new QName("FROM"));
			if (from == null){
				// FROM is mandatory
			  throw new IllegalArgumentException ("Attribute FROM missing in TAGENTRY");
			}
			// TO
			Attribute to = event.getAttributeByName(new QName("TO"));
			if (to == null){
				// TO is mandatory
			  throw new IllegalArgumentException ("Attribute TO missing in TAGENTRY");
			}
			IomObject entry=newIomObject("iom04.metamodel.TagEntry",null);
			parser_addAttrValue(entry,"from",from.getValue());
			parser_addAttrValue(entry,"to",to.getValue());
			parser_addAttrValue(hsAliasEntries,"entry",entry);
			return;
		}
		if(state==HS_ENTRIES && name.equals("VALENTRY")){
			state=HS_VALENTRY;
			// ATTR
			Attribute attr = event.getAttributeByName(new QName("ATTR"));
			if (attr == null){
				// ATTR is mandatory
			  throw new IllegalArgumentException ("Attribute ATTR missing in VALENTRY");
			}
			// FROM
			Attribute from = event.getAttributeByName(new QName("FROM"));
			if (from == null){
				// FROM is mandatory
			  throw new IllegalArgumentException ("Attribute FROM missing in VALENTRY");
			}
			// TO
			Attribute to = event.getAttributeByName(new QName("TO"));
			if (to == null){
				// TO is mandatory
			  throw new IllegalArgumentException ("Attribute TO missing in VALENTRY");
			}
			String classTag;
			String attrName;
			String attrVal=attr.getValue();
			// TAG
			if(!ili22){
				Attribute tag = event.getAttributeByName(new QName("TAG"));
				if (tag == null){
					// TAG is mandatory
				  throw new IllegalArgumentException ("Attribute TAG missing in VALENTRY");
				}
				classTag=tag.getValue();
				attrName=attrVal;
			}else{
				int sep=attrVal.lastIndexOf('.');
				classTag=attrVal.substring(0,sep);
				attrName=attrVal.substring(sep+1);
			}
			IomObject entry=newIomObject("iom04.metamodel.ValEntry",null);
			parser_addAttrValue(entry,"class",classTag);
			parser_addAttrValue(entry,"attr",attrName);
			parser_addAttrValue(entry,"from",from.getValue());
			parser_addAttrValue(entry,"to",to.getValue());
			parser_addAttrValue(hsAliasEntries,"entry",entry);
			return;
		}
		if(state==HS_ENTRIES && name.equals("DELENTRY")){
			state=HS_DELENTRY;
			// TAG
			Attribute tag = event.getAttributeByName(new QName("TAG"));
			if (tag == null){
				// TAG is mandatory
			  throw new IllegalArgumentException ("Attribute TAG missing in DELENTRY");
			}
			// ATTR
			Attribute attr = null;
			String classTag=null;
			String attrName=null;
			if(!ili22){
				attr = event.getAttributeByName(new QName("ATTR"));
				classTag=tag.getValue();
				if(attr!=null){
					attrName=attr.getValue();
				}
			}else{
				String attrVal=tag.getValue();
				int sep=attrVal.lastIndexOf('.');
				classTag=attrVal.substring(0,sep);
				attrName=attrVal.substring(sep+1);
			}
			IomObject entry=newIomObject("iom04.metamodel.DelEntry",null);
			parser_addAttrValue(entry,"class",classTag);
			if(attrName!=null){
				parser_addAttrValue(entry,"attr",attrName);
			}
			parser_addAttrValue(hsAliasEntries,"entry",entry);
			return;
		}
		if((state==HS_AFTER_MODELS || state==HS_AFTER_ALIAS) && name.equals("OIDSPACES")){
			state=HS_OIDSPACES;
			// [DEFAULT]
			Attribute defModel = event.getAttributeByName(new QName("DEFAULT"));
			hsOidSpaces=newIomObject("iom04.metamodel.OidSpaces",hsNextOid());
			if(defModel!=null){
				parser_addAttrValue(hsOidSpaces,"default",defModel.getValue());
			}
			header.put(hsOidSpaces.getobjectoid(),hsOidSpaces);
			return;
		}
		if(state==HS_OIDSPACES && name.equals("OIDSPACE")){
			state=HS_OIDSPACE;
			// NAME
			Attribute oidName = event.getAttributeByName(new QName("NAME"));
			if (oidName == null){
				// NAME is mandatory
			  throw new IllegalArgumentException ("Attribute NAME missing in OIDSPACE");
			}
			// OIDDOMAIN
			Attribute oidUri = event.getAttributeByName(new QName("OIDDOMAIN"));
			if (oidUri == null){
				// URI is mandatory
			  throw new IllegalArgumentException ("Attribute OIDDOMAIN missing in OIDSPACE");
			}
			IomObject entry=newIomObject("iom04.metamodel.OidSpaces",null);
			parser_addAttrValue(entry,"name",oidName.getValue());
			parser_addAttrValue(entry,"uri",oidUri.getValue());
			parser_addAttrValue(hsOidSpaces,"oidSpace",entry);
			((XtfStartTransferEvent)returnObject).addOidSpace(new OidSpace(oidName.getValue(),oidUri.getValue()));
			return;
		}
		if((state==HS_AFTER_ALIAS || state==HS_AFTER_MODELS || state==HS_AFTER_OIDSPACES)&& name.equals("COMMENT")){
			state=HS_COMMENT;
			// ensure we save collected characters only inside COMMENT
			propertyValue=null;
			return;
		}
    	/*
    	 * DATASECTION
    	 */
    	if(state==BEFORE_BASKET){
            if(filterTopics==null || filterTopics.contains(name)) {
                
            }else {
                skip=1;
                return;
            }
			Attribute bid = event.getAttributeByName(new QName("BID"));
	        if (bid == null){
	        	// BID is mandatory
	          throw new IllegalArgumentException ("Attribute BID missing in basket "+name);
	        }
	        String bidVal=stripX(bid.getValue());
			StartBasketEvent dataContainer = new StartBasketEvent(name,bidVal);
			Attribute startstate = event.getAttributeByName(new QName("STARTSTATE"));
	        if (startstate != null){
	        	dataContainer.setStartstate(startstate.getValue());
	        }
			Attribute endstate = event.getAttributeByName(new QName("ENDSTATE"));
	        if (endstate != null){
	        	dataContainer.setEndstate(endstate.getValue());
	        }
			Attribute kindXmlAttr = event.getAttributeByName(new QName("KIND"));
	        if (kindXmlAttr != null){
	        	String kindVal=kindXmlAttr.getValue();
	        	int kind=IomConstants.IOM_FULL;
	        	if(kindVal!=null){
	        		if(kindVal.equals("FULL")){
	        			kind=IomConstants.IOM_FULL;
	        		}else if(kindVal.equals("UPDATE")){
	        			kind=IomConstants.IOM_UPDATE;
	        		}else if(kindVal.equals("INITIAL")){
	        			kind=IomConstants.IOM_INITIAL;
	        		}else{
	        			EhiLogger.logError(event.getLocation().getLineNumber()+" illegal value <"+kindVal+"> for attribute KIND");
	        		}
	        	}
	        	dataContainer.setKind(kind);
	        }
			Attribute consistencyXmlAttr = event.getAttributeByName(new QName("CONSISTENCY"));
	        if (consistencyXmlAttr != null){
	        	String value=consistencyXmlAttr.getValue();
	        	int consistency=IomConstants.IOM_COMPLETE;
	    		if(value!=null){
	    			if(value.equals("INCOMPLETE")){
	    				consistency=IomConstants.IOM_INCOMPLETE;
	    			}else if(value.equals("COMPLETE")){
	    					consistency=IomConstants.IOM_COMPLETE;
	    			}else if(value.equals("INCONSISTENT")){
	    				consistency=IomConstants.IOM_INCONSISTENT;
	    			}else if(value.equals("ADAPTED")){
	    				consistency=IomConstants.IOM_ADAPTED;
	    			}else{
	        			EhiLogger.logError(event.getLocation().getLineNumber()+" illegal value <"+value+"> for attribute CONSISTENCY");
	    			}
	    		}
	        	dataContainer.setConsistency(consistency);
	        }
			//dataContainer.setFilename(this.reader.fileName);
	  		state=BEFORE_OBJECT;
			stopParser=true;
			returnObject=dataContainer;
	        return;
    	}
    	// SegmentSequence
		if(state==SS_AFTER_COORD){
			pushReturnState(SS_AFTER_COORD);
			if(name.equals("COORD")){
				state=CV_COORD;
				object=newIomObject("COORD",null);
			}else{
				state=ST_BEFORE_PROPERTY;
				object=newIomObject(name,null);
			}
			return;
		}
    	
    	// PolylineValue
		if((state==PV_POLYLINE 
				|| state==PV_AFTER_LINEATTR) 
				&& name.equals("CLIPPED")){
			state=PV_CLIPPED;
			changeReturnState(PV_AFTER_CLIPPED);
			object.setobjectconsistency(IomConstants.IOM_INCOMPLETE);
			return;
		}
		if(state==PV_POLYLINE && name.equals("LINEATTR")){
			state=PV_LINEATTR;
			return;
		}
		if(state==PV_LINEATTR){
			pushReturnState(PV_AFTER_LINEATTRSTRUCT);
			state=ST_BEFORE_PROPERTY;
			object=newIomObject(name,null);
			return;
		}
		if(state==PV_AFTER_CLIPPED && name.equals("CLIPPED")){
			state=PV_CLIPPED;
			pushReturnState(PV_AFTER_CLIPPED);
			return;
		}
		if((state==PV_POLYLINE 
				|| state==PV_CLIPPED 
				|| state==PV_AFTER_LINEATTR)
				&& name.equals("COORD")){
			pushReturnState(SS_AFTER_COORD);
			object=newIomObject("SEGMENTS",null);
			Element ele=new Element();
			ele.object=object;
			ele.propertyName="segment";
			objStack.add(0,ele);
			state=CV_COORD;
			object=newIomObject("COORD",null);
			return;
		}
		
		// SurfaceValue
		if(state==SV_SURFACE && name.equals("CLIPPED")){
			state=SV_CLIPPED;
			changeReturnState(SV_AFTER_CLIPPED);
			objStack.get(1).object.setobjectconsistency(IomConstants.IOM_INCOMPLETE);
			return;
		}
		if(state==SV_AFTER_CLIPPED && name.equals("CLIPPED")){
			pushReturnState(SV_AFTER_CLIPPED);
			state=SV_CLIPPED;
			object=newIomObject("SURFACE",null);
			Element ele=new Element();
			ele.object=object;
			ele.propertyName="boundary";
			objStack.add(0,ele);
			return;
		}
		if((state==SV_SURFACE || state==SV_CLIPPED || state==BD_AFTER_BOUNDARY)&& name.equals("BOUNDARY")){
			object=newIomObject("BOUNDARY",null);
			Element ele=new Element();
			ele.object=object;
			ele.propertyName="polyline";
			objStack.add(0,ele);
			state=BD_BOUNDARY;
			return;
		}
		if((state==BD_BOUNDARY || state==BD_AFTER_POLYLINE)&& name.equals("POLYLINE")){
			pushReturnState(BD_AFTER_POLYLINE);
			state=PV_POLYLINE;
			object=newIomObject("POLYLINE",null);
			Element ele=new Element();
			ele.object=object;
			ele.propertyName="sequence";
			objStack.add(0,ele);
			return;
		}
    	
    	// CoordValue
		if(state==CV_COORD && name.equals("C1")){
			state=CV_C1;
			// ensure we save collected characters only inside C1
			propertyValue=null;
			return;
		}
		if(state==CV_AFTER_C1 && name.equals("C2")){
			state=CV_C2;
			// ensure we save collected characters only inside C2
			propertyValue=null;
			return;
		}
		if(state==CV_AFTER_C2 && name.equals("C3")){
			state=CV_C3;
			// ensure we save collected characters only inside C3
			propertyValue=null;
			return;
		}
		if(state==ST_BEFORE_CHARACTERS && name.equals("SURFACE")){
			pushReturnState(ST_AFTER_SURFACE);
			state=SV_SURFACE;
			object=newIomObject("MULTISURFACE",null);
			Element ele=new Element();
			ele.object=object;
			ele.propertyName="surface";
			objStack.add(0,ele);
			object=newIomObject("SURFACE",null);
			ele=new Element();
			ele.object=object;
			ele.propertyName="boundary";
			objStack.add(0,ele);
			return;
		}
		if(state==ST_BEFORE_CHARACTERS && name.equals("POLYLINE")){
			pushReturnState(ST_AFTER_POLYLINE);
			state=PV_POLYLINE;
			object=newIomObject("POLYLINE",null);
			Element ele=new Element();
			ele.object=object;
			ele.propertyName="sequence";
			objStack.add(0,ele);
			return;
		}
		if(state==ST_BEFORE_CHARACTERS && name.equals("COORD")){
			pushReturnState(ST_AFTER_COORD);
			state=CV_COORD;
			object=newIomObject("COORD",null);
			return;
		}
		
		//BlackboxValue
		if(state==ST_BEFORE_CHARACTERS && name.equals("XMLBLBOX")){
			state=BB_XMLBLBOX;
			// collect xml
			collectXml=1;
			propertyXmlString=new java.io.StringWriter();
			try {
				propertyXml=xmlOf.createXMLEventWriter(propertyXmlString);
				propertyXml.add(eventOf.createStartDocument());
			} catch (XMLStreamException e) {
				throw new IoxException(e);
			}
			return;
		}
		if(state==ST_BEFORE_CHARACTERS && name.equals("BINBLBOX")){
			state=BB_BINBLBOX;
			// ensure we save collected characters only inside BINBLBOX
			propertyValue=null;
			return;
		}

    	if(state==BEFORE_OBJECT || state==ST_AFTER_STRUCTVALUE || state==ST_BEFORE_CHARACTERS || state==ST_BEFORE_EMBASSOC){

	      		// start StructValue
				if(state==BEFORE_OBJECT){
					pushReturnState(BEFORE_OBJECT);	      		
				}else if(state==ST_AFTER_STRUCTVALUE){
					pushReturnState(ST_AFTER_STRUCTVALUE);	      		
				}else if(state==ST_BEFORE_CHARACTERS){
					pushReturnState(ST_AFTER_STRUCTVALUE);	      		
				}else if(state==ST_BEFORE_EMBASSOC){
					pushReturnState(ST_BEFORE_EMBASSOC);	      		
				}
				state=ST_BEFORE_PROPERTY;

				object=newIomObject(name,null);
				Attribute oid = event.getAttributeByName(new QName("TID"));
				Attribute objBid = event.getAttributeByName(new QName("BID"));
				
				javax.xml.stream.Location loc=event.getLocation();
				object.setobjectcol(loc.getColumnNumber());
				object.setobjectline(loc.getLineNumber());
				if(ili22 && oid==null && objStack.size()==0){
					throw new IllegalArgumentException ("Attribute OID missing in object "+loc.getLineNumber());
				}
				if(oid!=null){
					object.setobjectoid(stripX(oid.getValue()));
				}
		        if(objBid!=null){
		        	//object.set(stripX(objBid));
		        }
				Attribute consistencyXmlAttr = event.getAttributeByName(new QName("CONSISTENCY"));
		        if (consistencyXmlAttr != null){
		        	String value=consistencyXmlAttr.getValue();
		        	int consistency=IomConstants.IOM_COMPLETE;
		    		if(value!=null){
		    			if(value.equals("INCOMPLETE")){
		    				consistency=IomConstants.IOM_INCOMPLETE;
		    			}else if(value.equals("COMPLETE")){
		    					consistency=IomConstants.IOM_COMPLETE;
		    			}else if(value.equals("INCONSISTENT")){
		    				consistency=IomConstants.IOM_INCONSISTENT;
		    			}else if(value.equals("ADAPTED")){
		    				consistency=IomConstants.IOM_ADAPTED;
		    			}else{
		        			EhiLogger.logError(event.getLocation().getLineNumber()+" illegal value <"+value+"> for attribute CONSISTENCY");
		    			}
		    		}
		        	object.setobjectconsistency(consistency);
		        }
				Attribute operationXmlAttr = event.getAttributeByName(new QName("OPERATION"));
		        if (operationXmlAttr != null){
		        	String value=operationXmlAttr.getValue();
		    		int operation=IomConstants.IOM_OP_INSERT;
		    		if(value!=null){
		    			if(value.equals("DELETE")){
		    				operation=IomConstants.IOM_OP_DELETE;
		    			}else if(value.equals("INSERT")){
		    				operation=IomConstants.IOM_OP_INSERT;
		    			}else if(value.equals("UPDATE")){
		    				operation=IomConstants.IOM_OP_UPDATE;
		    			}else{
		    				EhiLogger.logError(loc.getLineNumber()+" illegal value <"+value+"> for attribute OPERATION");
		    			}
		    		}
		        	object.setobjectoperation(operation);
		        }
		      return;
    	}
    	if(state==ST_BEFORE_PROPERTY){
    		if(object==null){
    			throw new IllegalStateException();
    		}
			// attribute ->characters 
			// struct ->startElement
			// ref (refattr) ->endElement
			// ref (role) ->endElement
			// ref (embedded assoc) ->startElement or EndElement
    		// oid (oiddomain) -> endElement
			Attribute oid=event.getAttributeByName(new QName("REF"));
			Attribute bid=null;
			if(oid==null){
				oid=event.getAttributeByName(new QName("EXTREF"));
				bid=event.getAttributeByName(new QName("BID"));
			}
			// save name,oid,bid
			// push state
			Element ele=new Element();
			ele.object=object;
			ele.propertyName=name;
			if(oid!=null){
				if(bid!=null){
					ele.bid=stripX(bid.getValue());
				}
				ele.oid=stripX(oid.getValue());
			}
			objStack.add(0,ele);
			object=null;
			if(oid!=null){
				state=ST_BEFORE_EMBASSOC;
			}else{
				state=ST_BEFORE_CHARACTERS;
			}
			// ensure we save collected characters only inside 
			// a property and not after a struct value
			propertyValue=null; 
			if(state==ST_BEFORE_CHARACTERS){
				oid=event.getAttributeByName(new QName("OID"));
				if(oid!=null){
					propertyValue=new StringBuilder(); 
					propertyValue.append(stripX(oid.getValue()));
					state=ST_AFTER_OID;
				}
			}
			return;
    	}
    	skip=1;

    }


	public void endElement(EndElement event) 
	throws IoxException
    {
    	level--;
		if(skip>0){
			skip--;
			return;
		}
		if(collectXml>0){
			collectXml--;
			if(collectXml>0){
	    		try {
					propertyXml.add(event);
				} catch (XMLStreamException e) {
					throw new IoxException(e);
				}
	    		return;
			}
		}
		
		/* DATASECTION
		 */
		// SegmentSequence
		if(state==SS_AFTER_COORD){
			popReturnState();
			if(state==ST_AFTER_POLYLINE){
				Element ele=objStack.remove(0);
				object=ele.object; // SEGMENTS			
				ele=objStack.remove(0);
				parser_addAttrValue(ele.object,ele.propertyName,object);
				object=ele.object;	// POLYLINE		
			}else if(state==BD_AFTER_POLYLINE){
					Element ele=objStack.remove(0);
					object=ele.object; // SEGMENTS			
					ele=objStack.remove(0);
					parser_addAttrValue(ele.object,ele.propertyName,object);
					object=ele.object;	// POLYLINE		
					ele=objStack.get(0);
					parser_addAttrValue(ele.object,ele.propertyName,object);
					object=null;
			}else if(state==PV_AFTER_CLIPPED){
				Element ele=objStack.remove(0);
				object=ele.object;	// SEGMENTS		
				ele=objStack.get(0);
				parser_addAttrValue(ele.object,ele.propertyName,object);
				object=null;			
			}else{
				throw new IllegalStateException();
			}			
		}else if(state==PV_AFTER_CLIPPED){
			Element ele=objStack.remove(0);
			object=ele.object;	// POLYLINE		
			state=ST_AFTER_POLYLINE;
		}else if(state==PV_AFTER_LINEATTRSTRUCT){
			state=PV_AFTER_LINEATTR;
		// Boundaries
		}else if(state==BD_AFTER_POLYLINE){
			Element ele=objStack.remove(0);
			object=ele.object; // BOUNDARY
			ele=objStack.get(0);			
			parser_addAttrValue(ele.object,ele.propertyName,object);
			//Dumper dumper=new Dumper();
			//dumper.dumpObject(System.err,ele.object);
			object=null;
			state=BD_AFTER_BOUNDARY;
		// SurfaceValue
		}else if(state==BD_AFTER_BOUNDARY){
			popReturnState();
			if(state==ST_AFTER_SURFACE){
				Element ele=objStack.remove(0);
				object=ele.object; // SURFACE
				ele=objStack.get(0);
				parser_addAttrValue(ele.object,ele.propertyName,object);
			}else if(state==SV_AFTER_CLIPPED){
				Element ele=objStack.remove(0);
				object=ele.object; // SURFACE
				ele=objStack.get(0);
				parser_addAttrValue(ele.object,ele.propertyName,object);
			}else{
				throw new IllegalStateException("state "+state);
			}
		}else if(state==SV_AFTER_CLIPPED){
			state=ST_AFTER_SURFACE;
		// CoordValue
		}else if(state==CV_AFTER_C1 || state==CV_AFTER_C2 || state==CV_AFTER_C3){
			popReturnState();
			if(state==SS_AFTER_COORD){
				// part of SEGMENTS
				Element ele=objStack.get(0);
				parser_addAttrValue(ele.object,ele.propertyName,object);
				//Dumper dumper=new Dumper();
				//dumper.dumpObject(System.err,ele.object);
				object=null;
			}else if(state==ST_AFTER_COORD){
			}else{
				throw new IllegalStateException();
			}			
		}else if(state==CV_C1){
			parser_addAttrValue(object,"C1",propertyValue.toString());
			propertyValue=null;
			state=CV_AFTER_C1;
		}else if(state==CV_C2){
			parser_addAttrValue(object,"C2",propertyValue.toString());
			propertyValue=null;
			state=CV_AFTER_C2;
		}else if(state==CV_C3){
			parser_addAttrValue(object,"C3",propertyValue.toString());
			propertyValue=null;
			state=CV_AFTER_C3;
		// BlackboxValue
		}else if(state==BB_BINBLBOX){
			Element ele=objStack.remove(0);
			String value=null;
			if(propertyValue!=null){
				value=propertyValue.toString();
			}
			parser_addAttrValue(ele.object,ele.propertyName,value);			
			object=ele.object;
			propertyValue=null;
			state=ST_AFTER_BINBLBOX;
		}else if(state==BB_XMLBLBOX){
			Element ele=objStack.remove(0);
			String value=null;
			if(propertyXmlString!=null){
				try {
					propertyXml.add(eventOf.createEndDocument());
					propertyXml.flush();
				} catch (XMLStreamException e) {
					throw new IoxException(e);
				}
				value=propertyXmlString.toString();
			}
			parser_addAttrValue(ele.object,ele.propertyName,value);			
			object=ele.object;
			propertyXml=null;
			propertyXmlString=null;
			state=ST_AFTER_XMLBLBOX;
		}else if(state==ST_AFTER_BINBLBOX || state==ST_AFTER_XMLBLBOX){
			state=ST_BEFORE_PROPERTY;
		// StructValue
		}else if(state==ST_AFTER_STRUCTVALUE 
				|| state==ST_BEFORE_CHARACTERS
				|| state==ST_AFTER_OID){
			// attribute
			// struct
			Element ele=objStack.remove(0);
			if(state==ST_BEFORE_CHARACTERS || state==ST_AFTER_OID){
				// attribute
				// may be: illegal whitespace, legal whitespace, not whitespace
				String value=null;
				if(propertyValue!=null){
					value=propertyValue.toString();
				}
				parser_addAttrValue(ele.object,ele.propertyName,value);
    		}else{
				// bag of structvalues
				// added to ele.object in endElement of structvalue
    		}
			object=ele.object;
			propertyValue=null;
			state=ST_BEFORE_PROPERTY;
    	}else if(state==ST_BEFORE_EMBASSOC){
				// ref (refattr)
				// ref (role)
				// ref (embedded assoc) with or without assocattrs
				Element ele=objStack.remove(0);
				if(object==null){
					// ref (refattr)
					// ref (role)
					// ref (embedded assoc) without assocattrs
					object=newIomObject("REF",null);
				}else{
					// ref (embedded assoc) with assocattrs
				}
				object.setobjectrefoid(ele.oid);
				object.setobjectrefbid(ele.bid);
				parser_addAttrValue(ele.object,ele.propertyName,object);
				object=ele.object;
				propertyValue=null;
				state=ST_BEFORE_PROPERTY;
		}else if(state==ST_AFTER_COORD){
			// attr of type COORD
			Element ele=objStack.remove(0);
			parser_addAttrValue(ele.object,ele.propertyName,object);
			object=ele.object;
			state=ST_BEFORE_PROPERTY;
			propertyValue=null;
		}else if(state==ST_AFTER_POLYLINE){
			// attr of type POLYLINE
			Element ele=objStack.remove(0);
			parser_addAttrValue(ele.object,ele.propertyName,object);
			object=ele.object;			
			state=ST_BEFORE_PROPERTY;
			propertyValue=null;
		}else if(state==ST_AFTER_SURFACE){
			// attr of type SURFACE/AREA
			Element ele=objStack.remove(0);
			object=ele.object; // MULTISURFACE
			ele=objStack.remove(0);
			parser_addAttrValue(ele.object,ele.propertyName,object);
			object=ele.object;			
			state=ST_BEFORE_PROPERTY;
			propertyValue=null;
    	}else if(state==ST_BEFORE_PROPERTY){
			popReturnState();
			if(state==BEFORE_OBJECT){
				returnObject=new ObjectEvent(object);
				stopParser=true;
				object=null;
			}else{
				if(state==ST_AFTER_STRUCTVALUE){
					Element ele=objStack.get(0);
					parser_addAttrValue(ele.object,ele.propertyName,object);
					object=null;
				}else if(state==PV_AFTER_LINEATTRSTRUCT){
					Element ele=objStack.get(0);
					parser_addAttrValue(ele.object,"lineattr",object);
					object=null;
				}else if(state==SS_AFTER_COORD){
					// part of SEGMENTS
					Element ele=objStack.get(0);
					parser_addAttrValue(ele.object,ele.propertyName,object);
					//Dumper dumper=new Dumper();
					//dumper.dumpObject(System.err,ele.object);
					object=null;
				}
			}
    	}else if(state==BEFORE_OBJECT){
			returnObject=new EndBasketEvent();
			stopParser=true;
    		state=BEFORE_BASKET;
    	}else if(state==BEFORE_BASKET){
    		state=BEFORE_DATASECTION;
		/* HEADERSECTION
		 */
		}else if(state==HS_HEADERSECTION){
			state=BEFORE_DATASECTION;
    		stopParser=true;
		}else if(state==HS_AFTER_MODELS){
			state=BEFORE_DATASECTION;
    		stopParser=true;
		}else if(state==HS_MODELS){
			state=HS_AFTER_MODELS;
		}else if(state==HS_MODEL){
			state=HS_MODELS;
		}else if(state==HS_ALIAS){
			state=HS_AFTER_ALIAS;
		}else if(state==HS_ENTRIES){
			hsAliasEntries=null;
			state=HS_ALIAS;
		}else if(state==HS_TAGENTRY){
			state=HS_ENTRIES;
		}else if(state==HS_VALENTRY){
			state=HS_ENTRIES;
		}else if(state==HS_DELENTRY){
			state=HS_ENTRIES;
		}else if(state==HS_OIDSPACES){
			hsOidSpaces=null;
			state=HS_AFTER_OIDSPACES;
		}else if(state==HS_OIDSPACE){
			state=HS_OIDSPACES;
		}else if(state==HS_COMMENT){
			if(propertyValue!=null){
				String hsComment=propertyValue.toString();
				((StartTransferEvent)returnObject).setComment(hsComment);
			}
			propertyValue=null;
			state=HS_AFTER_COMMENT;
		}else if(state==HS_AFTER_ALIAS){
			state=BEFORE_DATASECTION;
    		stopParser=true;
		}else if(state==HS_AFTER_OIDSPACES){
			state=BEFORE_DATASECTION;
    		stopParser=true;
		}else if(state==HS_AFTER_COMMENT){
			state=BEFORE_DATASECTION;
    		stopParser=true;
    	}else if(state==BEFORE_DATASECTION){
			//startParser=true;
    		state=BEFORE_TRANSFER;
    		returnObject=new EndTransferEvent();
    		stopParser=true;
    	}
    }
	public void characters(Characters event) throws IoxException {
		if(collectXml>0){
			if(collectXml>1){
	    		try {
					propertyXml.add(event);
				} catch (XMLStreamException e) {
					throw new IoxException(e);
				}
			}
    		return;
		}
		if(state==ST_BEFORE_CHARACTERS
				|| state==CV_C1
				|| state==CV_C2
				|| state==CV_C3
				|| state==BB_BINBLBOX
				|| state==HS_COMMENT
				){
			if(propertyValue==null){
				propertyValue=new StringBuilder();
			}
			propertyValue.append(event.getData());
		}
	}
	public void otherEvents(XMLEvent event) throws IoxException {
		if(collectXml>0){
			if(event.getEventType()==XMLEvent.START_DOCUMENT || event.getEventType()==XMLEvent.END_DOCUMENT){
				// skip it
			}else{
	    		try {
					propertyXml.add(event);
				} catch (XMLStreamException e) {
					throw new IoxException(e);
				}
			}
    	}
	}
	String stripX(String oid){
		if(oid==null){
			return null;
		}
		if(!ili22){
			return oid;
		}
		// ASSERT: isIli22
		if (!oid.startsWith("x")){
			// leading 'x' is mandatory
		  throw new IllegalArgumentException ("OID requires a leading 'x'");
		  //return oid;
		}
		// remove leading 'x' to get real OID
		return oid.substring(1);
	}
	private void parser_addAttrValue(IomObject obj,String attrName,String value)
	{
		if(value!=null){
			obj.setattrvalue(attrName,value);
		}
	}
	private void parser_addAttrValue(IomObject obj,String attrName,IomObject value)
	{
		if(value!=null){
			obj.addattrobj(attrName,value);
		}
	}
	private ch.interlis.iox.IoxFactory factory=null;
	public void setFactory(ch.interlis.iox.IoxFactory factory){
		this.factory=factory;
	}
	private IomObject newIomObject(String type,String oid)
	throws IoxException
	{
		return factory.createIomObject(type,oid);
	}
    public void setTopicFilter(String[] topicNames) {
        this.filterTopics=new java.util.HashSet<String>();
        for(String topicName:topicNames) {
            filterTopics.add(topicName);
        }
    }
    public boolean isIli22() {
        return ili22;
    }
  };