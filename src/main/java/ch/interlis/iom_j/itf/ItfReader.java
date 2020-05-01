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


import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox.IoxDataPool;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iom_j.itf.impl.ItfLineCursor;
import ch.interlis.iom_j.itf.impl.ItfLineKind;
import ch.interlis.iom_j.itf.impl.ItfScanner;
import ch.interlis.iom.*;
import ch.interlis.ili2c.metamodel.*;

import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

/** This class implements an INTERLIS 1 reader.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 23.06.2006 $
 */
public class ItfReader implements ch.interlis.iox.IoxReader,IoxIliReader{
	private ItfScanner scanner=null;
	private ItfLineCursor itfLine=null;
	private int state=0;
	private String modelName=null;
	private String topicName=null;
	private String className=null;
	private int basketCount=0;
	private String bidPrefix=null;
	private TransferDescription td=null;
	private HashMap tag2class=null;
	private java.io.InputStream inStream=null;
	private IoxFactoryCollection factory=new  ch.interlis.iox_j.DefaultIoxFactoryCollection();
	private boolean readEnumValAsItfCode=false;
	private boolean renumberTids=false;
	private HashMap tid2tid=null; // map<String oldTid,String newTid>
	private IoxDataPool ioxDataPool=null;
    private boolean skipBasket=false;

	/** Creates a new reader.
	 * @param in Input stream to read from.
	 * @throws IoxException
	 */
	public ItfReader(java.io.InputStream in)
	throws IoxException
	{
		try{
			scanner=new ItfScanner(in);
		}catch(java.io.UnsupportedEncodingException ex){
			throw new IoxException(ex);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
		itfLine=new ItfLineCursor();
		state=10;
	}
	public ItfReader(java.io.InputStream in,LogEventFactory errFact)
	throws IoxException
	{
		try{
			scanner=new ItfScanner(in);
		}catch(java.io.UnsupportedEncodingException ex){
			throw new IoxException(ex);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
		itfLine=new ItfLineCursor();
		state=10;
	}
	/** Creates a new reader.
	 * @param inFile File to read from.
	 * @throws IoxException
	 */
	public ItfReader(java.io.File inFile)
	throws IoxException
	{
		try{
			inStream=new java.io.FileInputStream(inFile);
		}catch(java.io.FileNotFoundException ex){
			throw new IoxException(ex);
		}
		try{
			scanner=new ItfScanner(inStream);
		}catch(java.io.UnsupportedEncodingException ex){
			throw new IoxException(ex);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
		itfLine=new ItfLineCursor();
		state=10;
	}
	public ItfReader(java.io.File inFile,LogEventFactory errFact)
	throws IoxException
	{
		try{
			inStream=new java.io.FileInputStream(inFile);
		}catch(java.io.FileNotFoundException ex){
			throw new IoxException(ex);
		}
		try{
			scanner=new ItfScanner(inStream);
		}catch(java.io.UnsupportedEncodingException ex){
			throw new IoxException(ex);
		}catch(java.io.IOException ex){
			throw new IoxException(ex);
		}
		itfLine=new ItfLineCursor();
		state=10;
	}
	public void close()
	throws IoxException
	{
		if(scanner!=null){
			scanner.close();
		}
		if(inStream!=null){
			try{
				inStream.close();
			}catch(java.io.IOException ex){
				throw new IoxException(ex);
			}
			inStream=null;
		}
		scanner=null;
		itfLine=null;
		state=0;
		td=null;
		tag2class=null;
	}
	public ch.interlis.iox.IoxEvent read()
	throws IoxException
	{
		ch.interlis.iox.IoxEvent event=null;
		// current main object
		IomObject iomObj=null;
		AttributeDef surfaceOrAreaAttr=null;
		// polylineattrs of current table
		List polylineattrs=null;
		// current polyline attr in linev
		int polyAttrIdx=0;
		boolean polyAttrIs3D=false;
		IomObject polyAttrSequence=null;
		while(true){
			try{
				scanner.read(itfLine);
			}catch(IoxException ex){
				throw new IoxException("failed to read logical line",ex);
			}
			int kind=itfLine.getKind();
			// EhiLogger.debug("state "+state);
			switch(state){
				case 10:
					if(kind!=ItfLineKind.SCNT){
						throw new IoxException(itfLine.getLineNumber(),"SCNT expected");
					}
					event=new ItfStartTransferEvent();
					((StartTransferEvent)event).setComment(itfLine.getContent());
					state=20;
					break;
				case 20:
					if(kind==ItfLineKind.MTID){
						String propv[]=splitItfLine(itfLine.getContent());
						String modelId=propv[0];
						if(modelId==null){
							throw new IoxException(itfLine.getLineNumber(),"missing model identification");
						}
						((ItfStartTransferEvent)event).setModelId(modelId);
					}else if(kind==ItfLineKind.MOTR){
						((ItfStartTransferEvent)event).setModelDefinition(itfLine.getContent());
					}else{
						throw new IoxException(itfLine.getLineNumber(),"MTID or MOTR expected");
					}
					state=40;
					return event;
				case 40:
					if(kind!=ItfLineKind.MODL){
						throw new IoxException(itfLine.getLineNumber(),"MODL expected");
					}
					{
						String propv[]=splitItfLine(itfLine.getContent());
						modelName=propv[0];
						if(modelName==null){
							throw new IoxException(itfLine.getLineNumber(),"missing model name");
						}
					}
					state=41;
					break;
				case 41:
					if(kind==ItfLineKind.TOPI){
						String propv[]=splitItfLine(itfLine.getContent());
						topicName=propv[0];
						if(topicName==null){
							throw new IoxException(itfLine.getLineNumber(),"missing topic name");
						}
						if(renumberTids){
							tid2tid=new HashMap();
						}
						state=50;
						String topicQName=modelName+"."+topicName;
						if(filterTopics==null || filterTopics.contains(topicQName)) {
	                        if(bidPrefix==null){
	                            event=new StartBasketEvent(topicQName,"itf"+Integer.toString(basketCount++));
	                        }else{
	                            event=new StartBasketEvent(topicQName,bidPrefix+"."+topicName);
	                        }
                            skipBasket=false;
	                        return event;
						}else {
						    skipBasket=true;
						}
						break;
					}else if(kind==ItfLineKind.EMOD){
						state=120;
					}else{
						throw new IoxException(itfLine.getLineNumber(),"TOPI or EMOD expected");
					}
					break;
				case 50:
					if(kind==ItfLineKind.TABL){
						String propv[]=splitItfLine(itfLine.getContent());
						className=propv[0];
						if(className==null){
							throw new IoxException(itfLine.getLineNumber(),"missing table name");
						}
						EhiLogger.traceState(className+"...");
						state=60;
					}else if(kind==ItfLineKind.ETOP){
						state=41;
						if(skipBasket) {
						    skipBasket=false; 
						}else {
	                        return new EndBasketEvent();
						}
					}else{
						throw new IoxException(itfLine.getLineNumber(),"TABL or ETOP expected");
					}
					break;
				case 60:
					if(kind==ItfLineKind.ETAB){
						className=null;
						state=50;
					}else if(kind==ItfLineKind.PERI){
						state=62;
					}else if(kind==ItfLineKind.OBJE){
						String propv[]=splitItfLine(itfLine.getContent());
						String tid=propv[0];
						if(tid==null){
							throw new IoxException(itfLine.getLineNumber(),"missing tid");
						}
						if(!skipBasket) {
	                        if(tid2tid!=null){
	                            String tid2=newTid();
	                            tid2tid.put(className+":"+tid, tid2);
	                            tid=tid2;
	                        }
	                        String iliQName=modelName+"."+topicName+"."+className;
	                        iomObj=createIomObject(iliQName,tid);
	                        iomObj.setobjectline(itfLine.getLineNumber());
	                        if(td!=null){
	                            if(tag2class==null){
	                                tag2class=ModelUtilities.getTagMap(td);
	                            }
	                            if(!tag2class.containsKey(iliQName)){
	                                throw new IoxException(itfLine.getLineNumber(),"unknown class <"+iliQName+">");
	                            }
	                            Object aclassObj=tag2class.get(iliQName);
	                            AbstractClassDef aclass;
	                            if(aclassObj instanceof AbstractClassDef){
	                                // main table
	                                aclass=(AbstractClassDef)aclassObj;
	                                polylineattrs=ModelUtilities.getPolylineAttrs(aclass);
	                            }else{
	                                // SURFACE or AREA helper table
	                                surfaceOrAreaAttr=(AttributeDef)aclassObj;
	                                aclass=(AbstractClassDef)surfaceOrAreaAttr.getContainer();
	                                polylineattrs=new java.util.ArrayList();
	                                polylineattrs.add(surfaceOrAreaAttr);
	                            }
	                            // start with first polyline attr in linev
	                            polyAttrIdx=0;
	                            // area or surface table?
	                            if(surfaceOrAreaAttr!=null){
	                                int startLineAttr;
	                                // is SURFACE attr?
	                                if(surfaceOrAreaAttr.getDomainResolvingAliases() instanceof SurfaceType){
	                                    // attr is a SURFACE
	                                    // add REF to main table
	                                    String maintableref=ModelUtilities.getHelperTableMainTableRef(surfaceOrAreaAttr);
	                                    String ref=null;
	                                    if(propv.length<2) {
	                                        throw new IoxInvalidDataException(itfLine.getLineNumber(),"missing reference to maintable "+aclass.getScopedName());
	                                    }else {
	                                        ref=propv[1];
	                                        if(ref==null) {
	                                            throw new IoxInvalidDataException(itfLine.getLineNumber(),"missing reference to maintable "+aclass.getScopedName());
	                                        }
	                                    }
	                                    if(tid2tid!=null){
	                                        String oldRef=aclass.getName()+":"+ref;
	                                        if(!tid2tid.containsKey(oldRef)){
	                                            throw new IoxException(itfLine.getLineNumber(),"dangling reference <"+oldRef+">");
	                                        }
	                                        ref=(String)tid2tid.get(oldRef);
	                                    }
	                                    if(ref!=null) {
	                                        IomObject structvalue = createIomObject("REF", null);
	                                        structvalue.setobjectrefoid(ref);
	                                        iomObj.addattrobj(maintableref,structvalue);
	                                    }
	                                    // start with prop[2], prop[0] is TID, prop[1] is REF to main table
	                                    startLineAttr=2;
	                                }else{
	                                    // attr is an AREA
	                                    // start with prop[1], prop[0] is TID
	                                    startLineAttr=1;
	                                }
	                                // add line attributes
	                                SurfaceOrAreaType saType=(SurfaceOrAreaType)surfaceOrAreaAttr.getDomainResolvingAliases();
	                                Table lineAttrTable=saType.getLineAttributeStructure();
	                                if(lineAttrTable!=null){
	                                    setPrimAttrs(iomObj,propv,ModelUtilities.getIli1AttrList(lineAttrTable),startLineAttr);
	                                }
	                            }else{
	                                // start with prop[1], prop[0] is TID
	                                setPrimAttrs(iomObj,propv,ModelUtilities.getIli1AttrList(aclass),1);
	                            }
	                        }
	                        event=new ObjectEvent(iomObj);
						}
						if(scanner.nextKind()==ItfLineKind.STPT){
							state=100;
						}else if(scanner.nextKind()==ItfLineKind.ELIN){
							state=100;
						}else{
						    if(!skipBasket) {
	                            return event;
						    }
						}
					}else{
						throw new IoxException(itfLine.getLineNumber(),"OBJE, PERI or ETAB expected");
					}
					break;
				case 62:
					if(kind==ItfLineKind.ETAB){
						className=null;
						state=50;
					}else{
						throw new IoxException(itfLine.getLineNumber(),"ETAB expected");
					}
					break;
				case 100:
					if(kind==ItfLineKind.STPT){
					    if(!skipBasket) {
	                        // get coord
	                        //EhiLogger.debug("STPT");
	                        if(td!=null){
	                            String propv[]=splitItfLine(itfLine.getContent());
	                            AttributeDef polyAttr=(AttributeDef)polylineattrs.get(polyAttrIdx);
	                            Type type = Type.findReal (polyAttr.getDomain());
	                            Domain controlPointDomain = ((LineType)type).getControlPointDomain();
	                            CoordType coordType=(CoordType) Type.findReal (controlPointDomain.getType());
	                            polyAttrIs3D=coordType.getDimensions().length==3;
	                            {
	                                String polyAttrName=null;
	                                if(surfaceOrAreaAttr!=null && polyAttrIdx==0){
	                                    polyAttrName=ModelUtilities.getHelperTableGeomAttrName(surfaceOrAreaAttr);
	                                }else{
	                                    polyAttrName=polyAttr.getName();
	                                }
	                                                    IomObject polylineValue = createIomObject("POLYLINE", null);
	                                iomObj.addattrobj(polyAttrName,polylineValue);

	                                // unclipped polyline, add one sequence
	                                polyAttrSequence = createIomObject("SEGMENTS", null);
	                                polylineValue.addattrobj("sequence", polyAttrSequence);

	                                //int segc=obj.numCoords();
	                                //for(int segi=0;segi<segc;segi++){
	                                    // add control point
	                                                                        IomObject coordValue = createIomObject("COORD", null);

	                                    coordValue.setattrvalue("C1",propv[0]);
	                                    coordValue.setattrvalue("C2",propv[1]);
	                                    if(polyAttrIs3D && propv.length>=3){
	                                        coordValue.setattrvalue("C3",propv[2]);
	                                    }
	                                    //coordValue.setattrvalue("A1",Double.toString(ordv[i]));
	                                    //coordValue.setattrvalue("A2",Double.toString(ordv[i+1]));
	                                    //if(is3D){
	                                    //  // no A3 in XTF!
	                                    //}
	                                    polyAttrSequence.addattrobj("segment", coordValue); // This line moved
	                                //}
	                            }
	                        }
					    }
						state=100;
					}else if(kind==ItfLineKind.LIPT){
						// get coord
						//EhiLogger.debug("LIPT");
					    if(!skipBasket) {
	                        if(td!=null){
	                            String propv[]=splitItfLine(itfLine.getContent());
	                                                        IomObject coordValue = createIomObject("COORD", null);

	                            coordValue.setattrvalue("C1",propv[0]);
	                            coordValue.setattrvalue("C2",propv[1]);
	                            if(polyAttrIs3D && propv.length>=3){
	                                coordValue.setattrvalue("C3",propv[2]);
	                            }
	                                                        polyAttrSequence.addattrobj("segment", coordValue); // This line moved
	                        }
					    }
						state=100;
					}else if(kind==ItfLineKind.ARCP){
					    if(!skipBasket) {
	                        // get coord
	                        if(td!=null){
	                            String propv[]=splitItfLine(itfLine.getContent());
	                                                        IomObject coordValue = createIomObject("ARC", null);
	                            coordValue.setattrvalue("A1",propv[0]);
	                            coordValue.setattrvalue("A2",propv[1]);
	                                                        polyAttrSequence.addattrobj("segment", coordValue); // This line moved
	                        }
					    }
						state=101;
					}else if(kind==ItfLineKind.ELIN){
                        if(!skipBasket) {
                            // end of line OR empty line
                            // advance current attr to next in list of polyline attrs
                            polyAttrIdx++;
                            polyAttrSequence=null;
                        }
						if(scanner.nextKind()==ItfLineKind.STPT){
							// another line
							state=100;
						}else if(scanner.nextKind()==ItfLineKind.ELIN){
							// another empty line
							state=100;
						}else{
							state=60;
	                        if(!skipBasket) {
	                            return event;
	                        }
						}
					}else{
						throw new IoxException(itfLine.getLineNumber(),"STPT, LIPT, ARCP or ELIN expected");
					}
					break;
				case 101:
					if(kind!=ItfLineKind.LIPT){
						throw new IoxException(itfLine.getLineNumber(),"LIPT expected");
					}
					if(!skipBasket) {
	                    // get coord
	                    //EhiLogger.debug("LIPT");
	                    if(td!=null){
	                        String propv[]=splitItfLine(itfLine.getContent());
	                        IomObject coordValue=null;
	                        int last=polyAttrSequence.getattrvaluecount("segment")-1;
	                        coordValue=polyAttrSequence.getattrobj("segment",last);
	                        coordValue.setattrvalue("C1",propv[0]);
	                        coordValue.setattrvalue("C2",propv[1]);
	                        if(polyAttrIs3D && propv.length>=3){
	                            coordValue.setattrvalue("C3",propv[2]);
	                        }
	                    }
					}
					state=100;
					break;
				case 120:
					if(kind==ItfLineKind.MODL){
						String propv[]=splitItfLine(itfLine.getContent());
						modelName=propv[0];
						if(modelName==null){
							throw new IoxException(itfLine.getLineNumber(),"missing model name");
						}
						state=41;
					}else if(kind==ItfLineKind.ENDE || kind==ItfLineKind.EOF){
						state=0;
						return new EndTransferEvent();
					}else{
						throw new IoxException(itfLine.getLineNumber(),"MODL or ENDE expected");
					}
					break;
				default:
					throw new IllegalStateException("line "+itfLine.getLineNumber()+", state "+state);
			}
		}
	}
	/** set the primitve attribute values.
	 * @param iomObj
	 * @param prop list of primitve values as read from the itf file.
	 * @param attrlist list of attritbute descriptions list<ViewableTransferElement attrDesc>
	 * @param propStartIdx index of first primive value in prop list (prop[propStartIdx] describedBy attrlist[0])
	 * @throws IoxException
	 */
	private void setPrimAttrs(IomObject iomObj,String prop[],List attrlist,int propStartIdx0)
	throws IoxException
	{

		int propStartIdx=propStartIdx0;
		  Iterator iter = attrlist.iterator();

		  while (iter.hasNext ()){
			ViewableTransferElement obj = (ViewableTransferElement)iter.next();
			if (obj.obj instanceof AttributeDef){
			  AttributeDef attr = (AttributeDef) obj.obj;
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
					if(prop.length>propStartIdx+1){
						// always 2D even if domain of control points is 3D
						if(prop[propStartIdx]!=null && prop[propStartIdx+1]!=null){
                                                        IomObject coord = createIomObject("COORD", null);
							coord.setattrvalue("C1",prop[propStartIdx++]);
							coord.setattrvalue("C2",prop[propStartIdx++]);
                                                        iomObj.addattrobj(attr.getName(), coord); // This line moved
						}else{
							propStartIdx+=2;
						}
					}else{
						propStartIdx+=2;
					}
				}else if(type instanceof CoordType){
					boolean is3D=((CoordType)type).getDimensions().length==3;
					if(prop.length>propStartIdx+(is3D?2:1)){
						if(prop[propStartIdx]!=null && prop[propStartIdx+1]!=null && (!is3D || prop[propStartIdx+2]!=null)){
                                                        IomObject coord = createIomObject("COORD", null);
							coord.setattrvalue("C1",prop[propStartIdx++]);
							coord.setattrvalue("C2",prop[propStartIdx++]);
							if(is3D && prop.length>=propStartIdx+1){
								coord.setattrvalue("C3",prop[propStartIdx++]);
							}
                                                        iomObj.addattrobj(attr.getName(), coord); // This line moved
						}else{
							propStartIdx+=(is3D?3:2);
						}
					}else{
						propStartIdx+=(is3D?3:2);
					}
				}else if(type instanceof EnumerationType){
					if(prop.length>propStartIdx && prop[propStartIdx]!=null){
						// map itf-code to ili-qname
						String itfCode=prop[propStartIdx++];
						// leading 0?
						if(itfCode.charAt(0)=='0' && itfCode.length()>1){
							try{
								// remove leading 0
								int code=Integer.parseInt(itfCode);
								itfCode=Integer.toString(code);
							}catch(NumberFormatException ex){
								// ignore error
							}
						}
						if(readEnumValAsItfCode){
							String iliQName=mapItfCode2XtfCode((EnumerationType)type,itfCode);
							if(iliQName==null){
								EhiLogger.logAdaption(iomObj.getobjecttag()+" "+iomObj.getobjectoid()+": unexpected code <"+itfCode+"> for attribute "+attr.getName()+"; unmodified read");
							}
							iomObj.setattrvalue(attr.getName(),itfCode);
						}else{
							String iliQName=mapItfCode2XtfCode((EnumerationType)type,itfCode);
							if(iliQName==null){
								EhiLogger.logAdaption(iomObj.getobjecttag()+" "+iomObj.getobjectoid()+": unexpected code <"+itfCode+"> for attribute "+attr.getName()+"; read without mapping");
								iliQName=itfCode;
							}
							iomObj.setattrvalue(attr.getName(),iliQName);
						}
					}else{
						propStartIdx++;
					}
				}else if(type instanceof TextType){
					if(prop.length>propStartIdx && prop[propStartIdx]!=null){
						String txt=prop[propStartIdx++];
						txt=txt.replace(blankCode,' ');
						iomObj.setattrvalue(attr.getName(),txt);
					}else{
						propStartIdx++;
					}
				}else{
					if(prop.length>propStartIdx && prop[propStartIdx]!=null){
						iomObj.setattrvalue(attr.getName(),prop[propStartIdx++]);
					}else{
						propStartIdx++;
					}
				}
			  }
			}else if(obj.obj instanceof RoleDef){
				RoleDef role = (RoleDef) obj.obj;
				if(!obj.embedded){
                                  IomObject structvalue = createIomObject("REF", null);
				  String ref=prop[propStartIdx++];
					if(tid2tid!=null){
						String oldRef=role.getDestination().getName()+":"+ref;
						if(!tid2tid.containsKey(oldRef)){
							throw new IoxException(itfLine.getLineNumber(),"dangling reference <"+oldRef+">");
						}
						ref=(String)tid2tid.get(oldRef);
					}
				   structvalue.setobjectrefoid(ref);
	                           iomObj.addattrobj(role.getName(), structvalue); // This line moved
				}else{
				  // +oppend.getName()+" ->"+oppend.getDestination().getName());
				  if(prop.length>propStartIdx && prop[propStartIdx]!=null){
	                                IomObject structvalue = createIomObject("REF", null);
					String ref=prop[propStartIdx++];
					if(tid2tid!=null){
						String oldRef=role.getDestination().getName()+":"+ref;
						if(!tid2tid.containsKey(oldRef)){
							throw new IoxException(itfLine.getLineNumber(),"dangling reference <"+oldRef+">");
						}
						ref=(String)tid2tid.get(oldRef);
					}
					structvalue.setobjectrefoid(ref);
                                        iomObj.addattrobj(role.getName(), structvalue); // This line moved
				  }else{
					  propStartIdx++;
				  }
				}
			}
		  }
		  if(prop.length!=propStartIdx){
				throw new IoxException(itfLine.getLineNumber(),"unexpected number of attribute values on logical line");
		  }
	}
	private String[] splitItfLine(String line)
	{
		String[] ret=line.trim().split("\\s+");
		for(int i=0;i<ret.length;i++){
			if(ret[i]!=null && ret[i].length()==0){
				ret[i]=null;
			}
			if(ret[i]!=null && ret[i].equals(undefinedCode)){
				ret[i]=null;
			}
		}
		return ret;
	}
	private String undefinedCode="@";
	private char blankCode='_';
	private char continueCode='\\';
	/** Sets the model of the the data to be read.
	 * @param td model as read by the compiler
	 */
	@Override
	public void setModel(TransferDescription td)
	{
		this.td=td;
		try{
			if(td!=null && td.getIli1Format()!=null){
				undefinedCode=ModelUtilities.code2string(td.getIli1Format().undefinedCode);
				blankCode=ModelUtilities.code2string(td.getIli1Format().blankCode).charAt(0);
				continueCode=ModelUtilities.code2string(td.getIli1Format().continueCode).charAt(0);
			}
			scanner.setContinueCode(continueCode);
		}catch(java.io.UnsupportedEncodingException ex){
			EhiLogger.logError(ex);
		}
	}
	public void setBidPrefix(String bidPrefix)
	{
		this.bidPrefix=bidPrefix;
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
	public void setReadEnumValAsItfCode(boolean val){
		readEnumValAsItfCode=val;
	}
	public boolean isReadEnumValAsItfCode(){
		return readEnumValAsItfCode;
	}
	static String dumpobject(IomObject obj)
	{
		StringBuffer ret=new StringBuffer();
		int i;
		IomObject structvalue;
		String value;
		String refoid;
		long orderPos;
		boolean doout=true;
		String className=obj.getobjecttag();
		String oid=obj.getobjectoid();
		ret.append(className);
		if(oid!=null){
			ret.append(" oid "+oid);
		}
		ret.append(" {");
		String sep="";
		for(i=0;i<obj.getattrcount();i++){
		   String propName=obj.getattrname(i);
		   value=obj.getattrvalue(propName);
		   if(value==null){
			  structvalue=obj.getattrobj(propName,0);
			  if(structvalue!=null){
					refoid=structvalue.getobjectrefoid();
					if(structvalue.getobjecttag()!=null){
						ret.append(sep+propName+" ");
						if(refoid!=null){
							orderPos=structvalue.getobjectreforderpos();
							if(orderPos!=0){
								ret.append("-> "+refoid+", ORDER_POS "+orderPos+" ");
							}else{
								ret.append("-> "+refoid+" ");
							}
						}
						ret.append(dumpobject(structvalue));
						if(refoid==null){
							int propc=obj.getattrvaluecount(propName);
							for(int propi=1;propi<propc;propi++){
								structvalue=obj.getattrobj(propName,propi);
								ret.append(", "+dumpobject(structvalue));
							}
						}
					}else if(refoid!=null){
						orderPos=structvalue.getobjectreforderpos();
						if(orderPos!=0){
							ret.append(sep+propName+" -> "+refoid+", ORDER_POS "+orderPos);
						}else{
							ret.append(sep+propName+" -> "+refoid);
						}
					}
					sep=", ";
			  }
		   }else{
			ret.append(sep+propName+" "+value);
			sep=", ";
		   }
		}
		ret.append("}");
		return ret.toString();
	}
	private EnumCodeMapper enumMapper=new EnumCodeMapper();
	private String mapItfCode2XtfCode(EnumerationType type,String xtfCode)
	{
		return enumMapper.mapItfCode2XtfCode(type, xtfCode);
	}
	public boolean isRenumberTids() {
		return renumberTids;
	}
	public void setRenumberTids(boolean renumberTids) {
		this.renumberTids = renumberTids;
	}
	private long tid=1;
    private java.util.Set<String> filterTopics=null;
	private String newTid(){
		return Long.toString(tid++);
	}
	/** maps a qualified interlis name to a ili2 metamodel object.
	 * @param iliQName qualified interlis1 name of maintable or surface/area attr
	 * @return Table or AttributeDef
	 */
	public Object mapIliQName2Class(String iliQName)
	{
		return tag2class.get(iliQName);
	}
	public IoxDataPool getIoxDataPool() {
		return ioxDataPool;
	}
	public void setIoxDataPool(IoxDataPool ioxDataPool) {
		this.ioxDataPool = ioxDataPool;
	}
    @Override
    public void setTopicFilter(String[] topicNames) {
        this.filterTopics=new java.util.HashSet<String>();
        for(String topicName:topicNames) {
            filterTopics.add(topicName);
        }
    }
    @Override
    public String getMimeType() {
        return ITF_10;
    }
}
