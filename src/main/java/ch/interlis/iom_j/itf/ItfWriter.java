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
import ch.interlis.iox_j.*;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iom.*;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.ItfRawWriter;

/** This class implements an INTERLIS 1 writer.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 17.07.2006 $
 */
public class ItfWriter implements ch.interlis.iox.IoxWriter {
	private String modelName=null;
	private String topicName=null;
	private String className=null;
	private ArrayList itftablev=null; // Array<Viewable|AttributeDef itfTable> list of tables according to ITF
	private int currentItfTable=-1;
	private TransferDescription td=null;
	private HashMap tag2class=null;
	private int state=0;
	private ItfStartTransferEvent itfStart=null;
	private char blankCode='_';
	private ItfRawWriter out=null;
	java.io.OutputStream outStream=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	
	/** Creates a new writer.
	 * @param out Output stream to write to
	 * @param td model of data to write as read by the compiler 
	 * @throws IoxException
	 */
	public ItfWriter(java.io.OutputStream out,TransferDescription td)
	throws IoxException 
	//java.io.UnsupportedEncodingException
	//,java.io.IOException
	{
		this.td=td;
		ch.interlis.ili2c.metamodel.Ili1Format format=td.getIli1Format();
		try{
			this.out=new ItfRawWriter(out,format);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
		try{
			if(format==null){
				blankCode=ModelUtilities.code2string(ch.interlis.ili2c.metamodel.Ili1Format.DEFAULT_BLANK_CODE).charAt(0);
			}else{
				blankCode=ModelUtilities.code2string(format.blankCode).charAt(0);
			}
		}catch(java.io.UnsupportedEncodingException ex){
			throw new IoxException(ex);
		}
	}
	/** Creates a new writer.
	 * @param outFile file to write to
	 * @param td model of data to write as read by the compiler 
	 * @throws IoxException
	 */
	public ItfWriter(java.io.File outFile,TransferDescription td)
	throws IoxException 
	{
		try{
			outStream=new java.io.FileOutputStream(outFile);
		}catch(java.io.FileNotFoundException ex){
			throw new IoxException(ex);
		}
		this.td=td;
		ch.interlis.ili2c.metamodel.Ili1Format format=td.getIli1Format();
		try{
			this.out=new ItfRawWriter(outStream,format);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
		try{
			if(format==null){
				blankCode=ModelUtilities.code2string(ch.interlis.ili2c.metamodel.Ili1Format.DEFAULT_BLANK_CODE).charAt(0);
			}else{
				blankCode=ModelUtilities.code2string(td.getIli1Format().blankCode).charAt(0);
			}
		}catch(java.io.UnsupportedEncodingException ex){
			throw new IoxException(ex);
		}
	}
	public void close()
		throws IoxException 
	{
		flush();
		if(out!=null){
			out.close();
			out=null;
		}
		if(outStream!=null){
			try{
				outStream.close();
			}catch(java.io.IOException ex){
				throw new IoxException(ex);
			}
			outStream=null;
		}
		td=null;
		state=0;
		tag2class=null;
		itfStart=null;
	}
	public void flush()
		throws IoxException 
	{
		if(out!=null){
			out.flush();
		}
		if(outStream!=null){
			try{
				outStream.flush();
			}catch(java.io.IOException ex){
				throw new IoxException(ex);
			}
		}
	}

	public void write(ch.interlis.iox.IoxEvent event) 
		throws IoxException 
	{
		if(event instanceof StartTransferEvent){
			if(state!=0){
				throw new IllegalStateException();
			}
			StartTransferEvent e=(StartTransferEvent)event;
			// SCNT
			try{
				out.writeRawText("SCNT");
				out.writeNewline();
				out.writeExplanation(e.getComment());
				out.writeRawText("////");
				out.writeNewline();
			}catch(IOException ex){
				throw new IoxException("failed to write SCNT section",ex);
			}
			// special kind (ITF specific) of start event?
			if(event instanceof ItfStartTransferEvent){
				itfStart=(ItfStartTransferEvent)event;
			}
			state=19;
		}else if(event instanceof StartBasketEvent){
			StartBasketEvent e=(StartBasketEvent)event;
			// first basket?
			if(state==19){
				if(itfStart!=null){
					// MTID/MOTR
					if(itfStart.getModelDefinition()!=null){
						try{
							out.writeRawText("MOTR");
							out.writeNewline();
							out.writeExplanation(itfStart.getModelDefinition());
							out.writeRawText("////");
							out.writeNewline();
						}catch(IOException ex){
							throw new IoxException("failed to write MOTR section",ex);
						}
					}else if(itfStart.getModelId()!=null){
						try{
							out.writeRawText("MTID ");
							out.writeRawText(itfStart.getModelId());
							out.writeNewline();
						}catch(IOException ex){
							throw new IoxException("failed to write MTID line",ex);
						}
					}else{
						throw new IllegalArgumentException();
					}
					String model[]=e.getType().split("\\.");
					modelName=model[0];
					// MODL
					try{
						out.writeRawText("MODL ");
						out.writeRawText(modelName);
						out.writeNewline();
					}catch(IOException ex){
						throw new IoxException("failed to write MODL line",ex);
					}
				}else{
					String model[]=e.getType().split("\\.");
					modelName=model[0];
					// MTID/MOTR
					try{
						out.writeRawText("MTID ");
						String tdName=td.getName();
						if(tdName==null){
							tdName=modelName;
						}
						out.writeRawText(tdName);
						out.writeNewline();
					}catch(IOException ex){
						throw new IoxException("failed to write MTID line",ex);
					}
					// MODL
					try{
						out.writeRawText("MODL ");
						out.writeRawText(modelName);
						out.writeNewline();
					}catch(IOException ex){
						throw new IoxException("failed to write MODL line",ex);
					}
				}
				state=20;
			}
			if(state!=20){
				throw new IllegalStateException();
			}
			String topic[]=e.getType().split("\\.");
			if(!topic[0].equals(modelName)){
				EhiLogger.logAdaption("unexpected model name <"+topic[0]+">; ignored");
			}
			topicName=topic[1]; // topic[0] is name of model
			// TOPI
			try{
				out.writeRawText("TOPI ");
				out.writeRawText(topicName);
				out.writeNewline();
			}catch(IOException ex){
				throw new IoxException("failed to write TOPI line",ex);
			}
			// setup list of itf tables
			itftablev=ModelUtilities.getItfTables(td,modelName,topicName);
			currentItfTable=-1;
			// setup trigger for first object of table 
			className=null;
			state=30;
		}else if(event instanceof ObjectEvent){
			if(state!=30){
				throw new IllegalStateException();
			}
			ObjectEvent e=(ObjectEvent)event;
			String classv[]=e.getIomObject().getobjecttag().split("\\.");
			if(!classv[0].equals(modelName)){
				EhiLogger.logAdaption("unexpected model name <"+classv[0]+">; ignored");
			}
			if(!classv[1].equals(topicName)){
				EhiLogger.logAdaption("unexpected topic name <"+classv[1]+">; ignored");
			}
			String nextClassName=classv[2];
			// first object of a table?
			if(className==null || !className.equals(nextClassName)){
				// not first table?
				if(className!=null){
					// ETAB
					try{
						out.writeRawText("ETAB");
						out.writeNewline();
					}catch(IOException ex){
						throw new IoxException("failed to write ETAB line",ex);
					}
				}
				// write empty tables
				int nextTableIdx=findItfTable(nextClassName);
				if(nextTableIdx==-1){
					throw new IoxException("unknown ITF table "+nextClassName);
				}
				if(nextTableIdx<currentItfTable){
					throw new IoxException("ITF table "+nextClassName+" already done; wrong order of objetcs");
				}
				while(currentItfTable+1<nextTableIdx){
					// write empty tables
					currentItfTable++;
					Element tabo=(Element)itftablev.get(currentItfTable);
					if(tabo instanceof AttributeDef){
						AttributeDef geomattr=(AttributeDef)tabo;
						Viewable aclass=(Viewable)geomattr.getContainer();
						if((aclass instanceof Table) && ((Table)aclass).isIli1Optional()){
							// skip it
						}else{
							writeEmptyTable(geomattr);
						}
					}else if((tabo instanceof Table) && ((Table)tabo).isIli1Optional()){
						// skip it
					}else{
						writeEmptyTable(tabo);
					}
				}
				currentItfTable++; // ==nextTableIdx
				className=nextClassName;
				// TABL
				try{
					out.writeRawText("TABL ");
					out.writeRawText(className);
					out.writeNewline();
				}catch(IOException ex){
					throw new IoxException("failed to write TABL line",ex);
				}
			}
			// OBJE
			writeObject(e.getIomObject());
		}else if(event instanceof EndBasketEvent){
			if(state!=30){
				throw new IllegalStateException();
			}
			// if end of table
			if(className!=null){
				// ETAB
				try{
					out.writeRawText("ETAB");
					out.writeNewline();
				}catch(IOException ex){
					throw new IoxException("failed to write ETAB line",ex);
				}
				currentItfTable++;
			}
            // write empty tables before ETOP
            while(currentItfTable<itftablev.size()){
                if(currentItfTable==-1) {
                    currentItfTable=0;
                }
                // write empty tables
                Element tabo=(Element)itftablev.get(currentItfTable);
                if(tabo instanceof AttributeDef){
                    AttributeDef geomattr=(AttributeDef)tabo;
                    Viewable aclass=(Viewable)geomattr.getContainer();
                    if((aclass instanceof Table) && ((Table)aclass).isIli1Optional()){
                        // skip it
                    }else{
                        writeEmptyTable(geomattr);
                    }
                }else if((tabo instanceof Table) && ((Table)tabo).isIli1Optional()){
                    // skip it
                }else{
                    writeEmptyTable(tabo);
                }
                currentItfTable++;
            }
			// ETOP
			try{
				out.writeRawText("ETOP");
				out.writeNewline();
			}catch(IOException ex){
				throw new IoxException("failed to write ETOP line",ex);
			}
			state=20;
		}else if(event instanceof EndTransferEvent){
			if(state!=19 && state!=20){
				throw new IllegalStateException();
			}
			// at least one Start/EndBasket received?
			if(state==20){
				try{
					out.writeRawText("EMOD");
					out.writeNewline();
				}catch(IOException ex){
					throw new IoxException("failed to write EMOD line",ex);
				}
			}
			try{
				out.writeRawText("ENDE");
				out.writeNewline();
			}catch(IOException ex){
				throw new IoxException("failed to write ENDE line",ex);
			}
		}
		
	}
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return factory.createIomObject(type, oid);
	}
	public IoxFactoryCollection getFactory() throws IoxException {
		return factory;
	}
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		this.factory=factory;
	}
	private void writeEmptyTable(Element tabo)
	throws IoxException
	{
		String tabName = getItfTableName(tabo);
		try {
			out.writeRawText("TABL ");
			out.writeRawText(tabName);
			out.writeNewline();
			out.writeRawText("ETAB");
			out.writeNewline();
		} catch (IOException ex) {
			throw new IoxException("failed to write empty table "+tabName,ex);
		}
	}
	private String getItfTableName(Element tabo) {
		String tabName;
		if(tabo instanceof AttributeDef){
			AttributeDef geomattr=(AttributeDef)tabo;
			Viewable aclass=(Viewable)geomattr.getContainer();
			tabName=aclass.getName()+"_"+geomattr.getName();
		}else{
			tabName=tabo.getName();
		}
		return tabName;
	}
	private int findItfTable(String tabName)
	{
		int tabc=itftablev.size();
		for(int tabi=0;tabi<tabc;tabi++){
			Element tabo=(Element)itftablev.get(tabi);
			String eleName = getItfTableName(tabo);
			if(eleName.equals(tabName)){
				// tabName found
				return tabi;
			}
		}
		// tabName not found
		return -1;
	}

	private void writeObject(IomObject iomObj)
	throws IoxException
	{
		try{
			// write primattr
			if(td!=null){
				String iliQName=iomObj.getobjecttag();
				if(tag2class==null){
					tag2class=ModelUtilities.getTagMap(td);
				}
				if(!tag2class.containsKey(iliQName)){
					throw new IoxException("unknown class <"+iliQName+">");
				}
				out.writeRawText("OBJE");
				out.writeTid(iomObj.getobjectoid());
				Object aclassObj=tag2class.get(iliQName);
				AbstractClassDef aclass;
				AttributeDef attrOfHelperTab=null;
				ArrayList linev=null;
				if(aclassObj instanceof AbstractClassDef){
					// main table
					aclass=(AbstractClassDef)aclassObj;
					linev=ModelUtilities.getPolylineAttrs(aclass);
				}else{
					// SURFACE or AREA helper table
					attrOfHelperTab=(AttributeDef)aclassObj;
					aclass=(AbstractClassDef)attrOfHelperTab.getContainer();
					linev=new ArrayList();
					linev.add(attrOfHelperTab);
				}
				// area or surface table?
				if(attrOfHelperTab!=null){
					// if SURFACE add REF to main table
					if(attrOfHelperTab.getDomainResolvingAliases() instanceof SurfaceType){
						String maintableref=ModelUtilities.getHelperTableMainTableRef(attrOfHelperTab);
						IomObject structvalue=iomObj.getattrobj(maintableref,0);
						String ref=structvalue.getobjectrefoid();
						out.writeTid(ref);
					}
					// line attributes
					SurfaceOrAreaType surfaceType=(SurfaceOrAreaType)attrOfHelperTab.getDomainResolvingAliases();
					Table lineAttrTable=surfaceType.getLineAttributeStructure();
					if(lineAttrTable!=null){
					    Iterator attri = lineAttrTable.getAttributes ();
					    while(attri.hasNext()){
					    	AttributeDef lineattr=(AttributeDef)attri.next();
							writeAttributeValue(iomObj, lineattr);
					    }
					}
				}else{
					ArrayList attrv=ModelUtilities.getIli1AttrList(aclass);
					Iterator attri=attrv.iterator();
					while (attri.hasNext ()){
					  ViewableTransferElement obj = (ViewableTransferElement)attri.next();
					  if (obj.obj instanceof AttributeDef){
						AttributeDef attr = (AttributeDef) obj.obj;
						writeAttributeValue(iomObj, attr);
					  }else if(obj.obj instanceof RoleDef){
						  RoleDef role = (RoleDef) obj.obj;
						  if(!obj.embedded){
							IomObject structvalue=iomObj.getattrobj(role.getName(),0);
							String ref=structvalue!=null ? structvalue.getobjectrefoid() : null;
							out.writeTid(ref);
						  }else{
							// +oppend.getName()+" ->"+oppend.getDestination().getName());
							  IomObject structvalue=iomObj.getattrobj(role.getName(),0);
							  String ref=structvalue!=null ? structvalue.getobjectrefoid() : null;
							out.writeTid(ref);
						  }
					  }
					}
				}
				out.writeNewline();
				// linev
				if(linev!=null){
					Iterator linei=linev.iterator();
					while(linei.hasNext()){
						AttributeDef polyAttr=(AttributeDef)linei.next();
						Type type = Type.findReal (polyAttr.getDomain());
						Domain controlPointDomain = ((LineType)type).getControlPointDomain();
						CoordType coordType=(CoordType) Type.findReal (controlPointDomain.getType());
						String polyAttrName=null;
						String errMsgAttrName=null;
						if(attrOfHelperTab!=null && polyAttr==attrOfHelperTab){
							polyAttrName=ModelUtilities.getHelperTableGeomAttrName(attrOfHelperTab);
							errMsgAttrName=attrOfHelperTab.getName();
						}else{
							polyAttrName=polyAttr.getName();
							errMsgAttrName=polyAttr.getName();
						}
						IomObject value=iomObj.getattrobj(polyAttrName,0);
						if(value!=null){
							writePolyline(value,errMsgAttrName,coordType);
						}
						out.writeRawText("ELIN");
						out.writeNewline();
					}
				}

			}else{
				out.writeRawText("OBJE");
				out.writeTid(iomObj.getobjectoid());
				out.writeNewline();
			}
		}catch(IOException ex){
			throw new IoxException("failed to write OBJE line",ex);
		}
	}
	private void writeAttributeValue(IomObject iomObj, AttributeDef attr) throws IOException, IoxException {
		Type type = Type.findReal (attr.getDomain());
		if((type instanceof LineType)
			&& !(type instanceof AreaType)){
			// LineType's but not AreaType
			; // not part of main record
		}else{
		  // AreaType: centroid is part of main record
		  // other non LineType's
		  // attr.getName()
		  if(type instanceof AreaType){
			  Domain controlPointDomain = ((AreaType)type).getControlPointDomain();
			  CoordType coordType=(CoordType) Type.findReal (controlPointDomain.getType());
			  IomObject coord=iomObj.getattrobj(attr.getName(),0);
				// always 2D even if domain of control points is 3D
				writeNum(iomObj,attr.getName(),coord!=null ? coord.getattrvalue(Iom_jObject.COORD_C1) : null,coordType.getDimensions()[0]);
				writeNum(iomObj,attr.getName(),coord!=null ? coord.getattrvalue(Iom_jObject.COORD_C2) : null,coordType.getDimensions()[1]);
		  }else if(type instanceof CoordType){
		  CoordType coordType=(CoordType)type;
			  boolean is3D=coordType.getDimensions().length==3;
			IomObject coord=iomObj.getattrobj(attr.getName(),0);
			writeNum(iomObj,attr.getName(),coord!=null ? coord.getattrvalue(Iom_jObject.COORD_C1) : null,coordType.getDimensions()[0]);
			writeNum(iomObj,attr.getName(),coord!=null ? coord.getattrvalue(Iom_jObject.COORD_C2) : null,coordType.getDimensions()[1]);
			if(is3D){
				writeNum(iomObj,attr.getName(),coord!=null ? coord.getattrvalue(Iom_jObject.COORD_C3) : null,coordType.getDimensions()[2]);
			}
		  }else if(type instanceof EnumerationType){
			String enumQName=iomObj.getattrvalue(attr.getName());
			  if(enumQName!=null){
				  // map ili-qname to itf-code 
				  String itfCode=mapXtfCode2ItfCode((EnumerationType)type,enumQName);
				  if(itfCode==null){
					  EhiLogger.logAdaption(iomObj.getobjecttag()+" "+iomObj.getobjectoid()+": unexpected code <"+enumQName+"> for attribute "+attr.getName()+"; written without mapping");
					itfCode=enumQName;
				  }
				out.writeValue(itfCode,false,getFieldSize(type));
			  }else{
				out.writeValue(null,false,getFieldSize(type));
			  }
		  }else if(type instanceof TextType){
				String txt=iomObj.getattrvalue(attr.getName());
				if(txt!=null){
					txt=txt.replace(' ',blankCode);
					out.writeValue(txt,false,((TextType)type).getMaxLength());
				}else{
					out.writeValue(null,false,((TextType)type).getMaxLength());
				}
		  }else if(type instanceof ReferenceType){
			String ref=iomObj.getattrvalue(attr.getName());
			out.writeTid(ref);
		  }else if(type instanceof NumericalType){
			String val=iomObj.getattrvalue(attr.getName());
			writeNum(iomObj,attr.getName(),val,(NumericalType)type);
		  }else{
			String txt=iomObj.getattrvalue(attr.getName());
			out.writeValue(txt,false,getFieldSize(type));
		  }
		}
	}
	private HashMap<Integer,DecimalFormat> numFmt=new HashMap<Integer,DecimalFormat>();
	private DecimalFormat getNumFmt(int accuracy)
	{
		if(numFmt.containsKey(accuracy)){
			return numFmt.get(accuracy);
		}
		DecimalFormat fmt = buildDecimalFormat(accuracy);
		numFmt.put(accuracy,fmt);
		return fmt;
	}
    public static DecimalFormat buildDecimalFormat(int accuracy) {
        DecimalFormatSymbols symbols=new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat fmt=null;
		if(accuracy==0){
			fmt=new DecimalFormat("#0",symbols);
		}else{
			fmt=new DecimalFormat("#0."+ch.ehi.basics.tools.StringUtility.STRING(accuracy,'0'),symbols);
		}
		fmt.setRoundingMode(RoundingMode.HALF_UP);
        return fmt;
    }
	private void writeNum(IomObject iomObj,String attrName,String numValue,NumericalType type1)
	throws java.io.IOException, IoxException
	{
		NumericType type=(NumericType)type1;
		if(numValue==null || numValue.length()==0){
			out.writeValue(null,true,getFieldSize(type));
			return;
		}
		int scalePos=numValue.toLowerCase().indexOf('e');
		if(scalePos>0){
			numValue=numValue.substring(0,scalePos);
		}
		int accuracy=type.getMinimum().getAccuracy();
		BigDecimal num=null;
		try{
			num=new BigDecimal(numValue);
		}catch(NumberFormatException e){
			  throw new IoxException(iomObj.getobjecttag()+" "+iomObj.getobjectoid()+": unexpected value <"+numValue+"> for attribute "+attrName);
		}
		DecimalFormat numFmt=getNumFmt(accuracy);
		numValue=numFmt.format(num);
		out.writeValue(numValue,true,getFieldSize(type));
	}
	private HashMap typeCache=new HashMap();
	private int getFieldSize(Type type)
	{
		if(typeCache.containsKey(type)){
			return ((Integer)typeCache.get(type)).intValue();
		}
		int size;
		if(type instanceof EnumerationType){
			ArrayList accu=new ArrayList();
			size=Integer.toString(ch.interlis.ili2c.generator.Interlis1Generator.countEnumLeafs(((EnumerationType)type).getEnumeration())).length();
		}else if(type instanceof NumericType){
			NumericType numType=(NumericType)type;
			String fmt=ch.interlis.ili2c.generator.Interlis1Generator.genFmtField(0,numType.getMinimum(),numType.getMaximum());
			size=fmt.length();			
		}else if(type instanceof FormattedType){
			FormattedType fmtType=(FormattedType)type;
			String min=fmtType.getMinimum();
			String max=fmtType.getMaximum();
			size=Math.max(min.length(), max.length());
		}else{
			throw new IllegalArgumentException("unknown type "+type.getClass().getName());
		}
		typeCache.put(type,new Integer(size));
		return size;
	}

	private void writePolyline(IomObject obj,String errMsgAttrName,CoordType coordType)
	throws java.io.IOException, IoxException
	{
		if(obj==null){
			return;
		}
		boolean is3D=coordType.getDimensions().length==3;
		boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		if(clipped){
			throw new IllegalArgumentException("IOM_INCOMPLETE not supported by ITF file format");
		}
		for(int sequencei=0;sequencei<obj.getattrvaluecount(Iom_jObject.POLYLINE_SEQUENCE);sequencei++){
			if(clipped){
				//out.startElement(tags::get_CLIPPED(),0,0);
			}else{
				// an unclipped polyline should have only one sequence element
				if(sequencei>0){
					EhiLogger.logError("unclipped polyline with multi 'sequence' elements");
					break;
				}
			}
			IomObject sequence=obj.getattrobj(Iom_jObject.POLYLINE_SEQUENCE,sequencei);
			for(int segmenti=0;segmenti<sequence.getattrvaluecount(Iom_jObject.SEGMENTS_SEGMENT);segmenti++){
				IomObject segment=sequence.getattrobj(Iom_jObject.SEGMENTS_SEGMENT,segmenti);
				if(segment.getobjecttag().equals(Iom_jObject.COORD)){
					// COORD
					if(segmenti==0){
						out.writeRawText("STPT");
					}else{
						out.writeRawText("LIPT");
					}
					writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.COORD_C1) ,coordType.getDimensions()[0]);
					writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.COORD_C2) ,coordType.getDimensions()[1]);
					if(is3D){
						writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.COORD_C3) ,coordType.getDimensions()[2]);
					}
					out.writeNewline();
				}else if(segment.getobjecttag().equals(Iom_jObject.ARC)){
					// ARC
					out.writeRawText("ARCP");
					writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.ARC_A1) ,coordType.getDimensions()[0]);
					writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.ARC_A2) ,coordType.getDimensions()[1]);
					//if(is3D){
					//	writeNum(segment.getattrvalue("A3") ,coordType.getDimensions()[2]);
					//}
					out.writeNewline();
					out.writeRawText("LIPT");
					writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.COORD_C1) ,coordType.getDimensions()[0]);
					writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.COORD_C2) ,coordType.getDimensions()[1]);
					if(is3D){
						writeNum(obj,errMsgAttrName,segment.getattrvalue(Iom_jObject.COORD_C3) ,coordType.getDimensions()[2]);
					}
					out.writeNewline();
				}else{
					// custum line form
					EhiLogger.logAdaption("custom line form not supported; ignored");
					//out.startElement(segment->getTag(),0,0);
					//writeAttrs(out,segment);
					//out.endElement(/*segment*/);
				}

			}
			if(clipped){
				//out.endElement(/*CLIPPED*/);
			}
		}
	}
	private EnumCodeMapper enumTypes=new EnumCodeMapper();
	private String mapXtfCode2ItfCode(EnumerationType type,String xtfCode)
	{
		return enumTypes.mapXtfCode2ItfCode(type, xtfCode);
	}	
}
