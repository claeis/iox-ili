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
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXResult;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.*;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.ViewableProperty;
import ch.interlis.iom_j.xtf.OidSpace;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.XtfStartTransferEvent;

import java.util.Iterator;

	/** Writer of an INTERLIS 2 transfer file.
	 * @author ceis
	 */
    public class XtfWriterAlt extends AbstractXtfWriterAlt
    {
        private ViewableProperties mapping = null;
        /** XML namespace of INTERLIS 2.2 transfer files.
         */
        public static final String ili22Ns="http://www.interlis.ch/INTERLIS2.2";
        /** XML namespace of INTERLIS 2.3 transfer files.
         */
        public static final String ili23Ns="http://www.interlis.ch/INTERLIS2.3";
        private static String iliNs=ili23Ns;
        private boolean isIli22=false;
		private XMLInputFactory inputFactory = null;
        
        /** Creates a new writer.
         * @param buffer Writer to write to
         * @param mapping1 model of data to write
         * @param version transfer format version (2.2 or 2.3)
         * @throws IoxException
         */
        public XtfWriterAlt(java.io.OutputStreamWriter buffer,ViewableProperties mapping1,String version)
		throws IoxException
        { 
            mapping = mapping1;
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			// TODO outputFactory.setProperty(com.ctc.wstx.api.WstxOutputProperties.P_OUTPUT_ESCAPE_CR,Boolean.FALSE);
			//if (useRepairing) {
			//	outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
			//}
			try{
				xout = outputFactory.createXMLStreamWriter(new java.io.BufferedWriter(buffer));
				// buffer.getEncoding() may return a historial name like "UTF8", translate this 
				// to a canonical one like "UTF-8"
				String encoding=java.nio.charset.Charset.forName(buffer.getEncoding()).name();
				xout.writeStartDocument(encoding,"1.0");
				//out.setPrefix("xtf",iliNs);
				if(version!=null && version.equals("2.2")){
					iliNs=ili22Ns;
					isIli22=true;
				}else{
					iliNs=ili23Ns;
					isIli22=false;
				}
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}

        }
        @Override
		public void writeStartTransfer(String sender, String comment,XtfModel models[])
		throws IoxException
		{
        	writeStartTransfer(sender, comment, models, null);
		}
        @Override
		public void writeStartTransfer(String sender, String comment,XtfModel models[],XtfStartTransferEvent e)
		throws IoxException
        {
			try{
				xout.setDefaultNamespace(iliNs);
				xout.writeStartElement(iliNs,"TRANSFER");
				//out.writeNamespace("xtf", iliNs);
				xout.writeDefaultNamespace(iliNs);
				newline();
				// HEADERSECTION
				{
					xout.writeStartElement(iliNs,"HEADERSECTION");
					if(sender==null || sender.length()==0){
						sender="IOX";
					}
					xout.writeAttribute("SENDER",sender);
					xout.writeAttribute("VERSION",isIli22?"2.2":"2.3");
					if(isIli22){
						xout.writeStartElement(iliNs,"ALIAS");
						xout.writeEndElement();
					}else{
						xout.writeStartElement(iliNs,"MODELS");
						if(models==null || models.length==0){
							throw new IoxException("no models given");
						}
						for(int i=0;i<models.length;i++){
							XtfModel model=models[i];
							xout.writeStartElement(iliNs,"MODEL");
							xout.writeAttribute("NAME",model.getName());
							xout.writeAttribute("VERSION",model.getVersion());
							xout.writeAttribute("URI",model.getUri());
							xout.writeEndElement();
							
						}
						xout.writeEndElement();
						// TODO ALIAS
						if(e!=null && e.getOidSpaces().size()>0){
							xout.writeStartElement(iliNs,"OIDSPACES");
							Iterator oidi=e.getOidSpaces().iterator();
							while(oidi.hasNext()){
								OidSpace oidspace=(OidSpace)oidi.next();
								xout.writeStartElement(iliNs,"OIDSPACE");
								xout.writeAttribute("NAME",oidspace.getName());
								xout.writeAttribute("OIDDOMAIN",oidspace.getOiddomain());
								xout.writeEndElement();
								
							}
							xout.writeEndElement();
						}
					}
					writeElementStringOptional("COMMENT",comment);
					xout.writeEndElement();
					newline();
				}

				// DATASECTION
				xout.writeStartElement(iliNs,"DATASECTION");
				newline();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}

        }
        @Override
		public void writeEndTransfer()
		throws IoxException
        {
			try{
				xout.writeEndElement(); // DATASECTION
				newline();
				xout.writeEndElement(); // TRANSFER
				xout.writeEndDocument();
				xout.flush();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
            xout=null;
        }
        @Override
		public void writeStartBasket(String type, String bid)
		throws IoxException
        {
            writeStartBasket(type,bid,IomConstants.IOM_COMPLETE, IomConstants.IOM_FULL,null,null, null,null);
        }
        @Override
		public void writeStartBasket(String type,String bid,int consistency, int kind,String startstate,String endstate, String[] topicv,String domains)
		throws IoxException
        {
			try{
				xout.writeStartElement(iliNs,type);
				xout.writeAttribute("BID", makeOid(bid));
				writeAttributeStringOptional("KIND", encodeBasketKind(kind));
				if (kind != IomConstants.IOM_FULL)
				{
					if(kind==IomConstants.IOM_UPDATE){
						xout.writeAttribute("STARTSTATE", startstate);
					}
					xout.writeAttribute("ENDSTATE", endstate);
				}
				writeAttributeStringOptional("CONSISTENCY", encodeConsistency(consistency));
				if (topicv != null && topicv.length > 0)
				{
					StringBuffer tb = new StringBuffer();
					String sep = "";
					for (int i = 0; i < topicv.length; i++)
					{
						String t = topicv[i];
						if (t != null)
						{
							t = t.trim();
							if (t.length() > 0)
							{
								tb.append(t);
								tb.append(sep); sep = ",";
							}
						}
					}
					writeAttributeStringOptional("TOPICS", tb.toString());
				}
				newline();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}

        }
        @Override
		public void writeEndBasket()
		throws IoxException
        {
			try{
				xout.writeEndElement();
				newline();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        @Override
		public void writeObject(IomObject obj)
		throws IoxException
        {
			try{
				xout.writeStartElement(iliNs,obj.getobjecttag());
				writeAttributeStringOptional("TID", makeOid(obj.getobjectoid()));
				//writeAttributeStringOptional("BID", makeOid(obj.getobjectbid()));
				writeAttributeStringOptional("OPERATION", encodeOperation(obj.getobjectoperation()));
				writeAttributeStringOptional("CONSISTENCY", encodeConsistency(obj.getobjectconsistency()));
				writeObjAttrs(obj);
				xout.writeEndElement();
				newline();
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        private void writeObjAttrs(IomObject obj)
		throws IoxException
        {
            String tag = obj.getobjecttag();
            // class known?
            if (mapping!=null && mapping.existsClass(tag))
            {
                ViewableProperty[] attrv = mapping.getClassVProperties(tag);
                for (int i = 0; i < attrv.length; i++)
                {
                    writeObjAttr(obj, attrv[i]);
                }
            }
            else
            {
                // unknown class; dump all attributes in undefined order
                // new unknown class?
                if (!unkClsv.contains(tag))
                {
                    EhiLogger.logError("unknown class " + tag);
                    // add it to list of unknown classes
                    unkClsv.add(tag);
                }
                for (int i = 0; i < obj.getattrcount(); i++)
                {
                    writeObjAttr(obj, new ViewableProperty(obj.getattrname(i)));
                }
            }
        }
        private void writeObjAttr(IomObject obj, ViewableProperty attr)
		throws IoxException
        {
        	String attrName=attr.getName();
			try{
				int valueCount=obj.getattrvaluecount(attrName);
				if(valueCount>0){
					String val=obj.getattrprim(attrName,0);
					// not a primitive?
					if(val==null){
						IomObject child=obj.getattrobj(attrName,0);
						if (child != null)
						{
							// some special cases
							if (child.getobjecttag().equals("COORD"))
							{
								// COORD
								xout.writeStartElement(iliNs,attrName);
								writeCoord(child);
								xout.writeEndElement(/*attr*/);
								if (valueCount > 1)
								{
									throw new IoxException("max one COORD value allowed ("+attrName+")");
								}
							}
							else if (child.getobjecttag().equals("POLYLINE"))
							{
								// POLYLINE
								xout.writeStartElement(iliNs,attrName);
								writePolyline(child, false);
								xout.writeEndElement(/*attr*/);
								if (valueCount > 1)
								{
									throw new IoxException("max one POLYLINE value allowed ("+attrName+")");
								}
							}
							else if (child.getobjecttag().equals("MULTISURFACE"))
							{
								// MULTISURFACE
								xout.writeStartElement(iliNs,attrName);
								writeSurface(child);
								xout.writeEndElement(/*attr*/);
								if (valueCount > 1)
								{
									throw new IoxException("max one MULTISURFACE value allowed ("+attrName+")");
								}
							}
							else
							{
								// normal case
								String aref = child.getobjectrefoid();
								boolean isRef = aref != null ? true : false;
								// Reference-attribute or Role or EmbeddedLink?
								if (isRef)
								{
									String orderpos = null;
									if (child.getobjectreforderpos() > 0)
									{
										orderpos = Long.toString(child.getobjectreforderpos());
									}
									String extref = null;
									String bid = child.getobjectrefbid();
									if (bid != null)
									{
										extref = aref;
										aref = null;
									}
									xout.writeStartElement(iliNs,attrName);
									if (aref != null)
									{
										xout.writeAttribute("REF", makeOid(aref));
									}
									else
									{
										xout.writeAttribute("EXTREF", makeOid(extref));
										xout.writeAttribute("BID", makeOid(bid));
									}
									writeAttributeStringOptional("ORDER_POS", orderpos);
									if (child.getattrcount() > 0)
									{
										xout.writeStartElement(iliNs,child.getobjecttag());
										writeObjAttrs(child);
										xout.writeEndElement(/*child*/);
									}
									xout.writeEndElement(/*attr*/);
									if (valueCount > 1)
									{
										throw new IoxException("max one reference value allowed ("+attrName+")");
									}
								}
								else
								{
									// struct
									xout.writeStartElement(iliNs,attrName);
									int valuei = 0;
									while (true)
									{
										xout.writeStartElement( iliNs,child.getobjecttag());
										writeObjAttrs(child);
										xout.writeEndElement(/*child*/);
										valuei++;
										if (valuei >= valueCount)
										{
											break;
										}
										child = obj.getattrobj(attrName, valuei);
									}
									xout.writeEndElement(/*attr*/);
								}
							}
						}

					}else{
						if(attr.isTypeOid()){
							val=val.trim();
							if(val.length()>0){
								xout.writeStartElement(iliNs,attrName);
								xout.writeAttribute("OID", makeOid(val));
								xout.writeEndElement(/*attr*/);
							}
						}else if(attr.isTypeBlackboxBin()){
							val=val.trim();
							if(val.length()>0){
								xout.writeStartElement(iliNs,attrName);
								xout.writeStartElement(iliNs,"BINBLBOX");
								xout.writeCharacters(val);
								int rest=val.length()%2;
								if(rest==2){
									xout.writeCharacters("==");
								}else if(rest==1){
									xout.writeCharacters("=");
								}
								xout.writeEndElement(/*BINBLBOX*/);
								xout.writeEndElement(/*attr*/);
							}
						}else if(attr.isTypeBlackboxXml()){
							val=val.trim();
							if(val.length()>0){
								xout.writeStartElement(iliNs,attrName);
								xout.writeStartElement(iliNs,"XMLBLBOX");
								// copy xml
								if(inputFactory==null){
									inputFactory = XMLInputFactory.newInstance();
								}
								XMLEventReader xmlReader=inputFactory.createXMLEventReader(new java.io.StringBufferInputStream(val));
								while(xmlReader.hasNext()){
									XMLEvent event=xmlReader.nextEvent();
									if(event.getEventType()==XMLEvent.START_DOCUMENT || event.getEventType()==XMLEvent.END_DOCUMENT){
										// skip it
									}else{
										XMLStreamWriterAdapter.add(xout, event);
									}
								}
								xout.writeEndElement(/*XMLBLBOX*/);
								xout.writeEndElement(/*attr*/);
							}
						}else{
							writeElementStringOptional(attrName,val);
						}
						if(valueCount>1){
							throw new IoxException("max one primitive-type value allowed ("+attrName+")");
						}
					}
				}
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        /** writes a coord value or a coord segment.
         */
        private void writeCoord(IomObject obj)
		throws IoxException
        {
        /*
             object: COORD
               C1
                 102.0
               C2
                 402.0
	        <COORD><C1>102.0</C1><C2>402.0</C2></COORD>
        */
		try{
			xout.writeStartElement(iliNs,"COORD");
			String c1=obj.getattrprim("C1",0);
			writeElementStringOptional("C1",c1);
			String c2=obj.getattrprim("C2",0);
			if(c2!=null){
				writeElementStringOptional("C2",c2);
				String c3=obj.getattrprim("C3",0);
				if(c3!=null){
					writeElementStringOptional("C3",c3);
				}
			}
			xout.writeEndElement(/*COORD*/);
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}

        }
        /** writes a arc segment value.
         */
        private void writeArc(IomObject obj)
		throws IoxException
        {
        /*
             object: ARC
               C1
                 103.0
               C2
                 403.0
               A1
                 104.0
               A2
                 404.0
	        <COORD><C1>103.0</C1><C2>403.0</C2><A1>104.0</A1><A2>404.0</A2></COORD>
        */
		try{
			xout.writeStartElement(iliNs,"ARC");
			String c1=obj.getattrprim("C1",0);
			writeElementStringOptional("C1",c1);
			String c2=obj.getattrprim("C2",0);
			writeElementStringOptional("C2",c2);
			String c3=obj.getattrprim("C3",0);
			if(c3!=null){
				writeElementStringOptional("C3",c3);
			}
			String a1=obj.getattrprim("A1",0);
			writeElementStringOptional("A1",a1);
			String a2=obj.getattrprim("A2",0);
			writeElementStringOptional("A2",a2);
			String r=obj.getattrprim("R",0);
			if(r!=null){
				writeElementStringOptional("R",r);
			}
			xout.writeEndElement(/*ARC*/);
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}

        }
        /** writes a polyline value.
         */
        private void writePolyline(IomObject obj,boolean hasLineAttr)
		throws IoxException
        {
        /*
             object: POLYLINE [INCOMPLETE]
               lineattr
                 object: Model.Topic.LineAttr
                   attr00
                     11
               sequence // if incomplete; multi sequence values
                 object: SEGMENTS
                   segment
                     object: COORD
                       C1
                         102.0
                       C2
                         402.0
                   segment
                     object: ARC
                       C1
                         103.0
                       C2
                         403.0
                       A1
                         104.0
                       A2
                         404.0
                   segment
                     object: Model.SplineParam
                       SegmentEndPoint
                         object: COORD
                           C1
                             103.0
                           C2
                             403.0
                       p0
                         1.0
                       p1
                         2.0

		        <POLYLINE>
			        <LINEATTR>
				        <Model.Topic.LineAttr>
					        <attr00>11</attr00>
				        </Model.Topic.LineAttr>
			        </LINEATTR>
			        <COORD>
				        <C1>101.0</C1>
				        <C2>401.0</C2>
			        </COORD>
			        <COORD>
				        <C1>102.0</C1>
				        <C2>402.0</C2>
			        </COORD>
			        <Model.SplineParam>
				        <SegmentEndPoint>
					        <COORD>
						        <C1>103.0</C1>
						        <C2>403.0</C2>
					        </COORD>
				        </SegmentEndPoint>
				        <p0>1.0</p0>
				        <p1>2.0</p1>
			        </Model.SplineParam>
		        </POLYLINE>
        */
		try{
			xout.writeStartElement(iliNs,"POLYLINE");
			if(hasLineAttr){
				IomObject lineattr=obj.getattrobj("lineattr",0);
				if(lineattr!=null){
					xout.writeStartElement(iliNs,"LINEATTR");
					xout.writeStartElement(iliNs,lineattr.getobjecttag());
					writeObjAttrs(lineattr);
					xout.writeEndElement(/*lineattr*/);
					xout.writeEndElement(/*LINEATTR*/);
				}
			}
			boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			for(int sequencei=0;sequencei<obj.getattrvaluecount("sequence");sequencei++){
				if(clipped){
					xout.writeStartElement(iliNs,"CLIPPED");
				}else{
					// an unclipped polyline should have only one sequence element
					if(sequencei>0){
						throw new IllegalArgumentException("unclipped polyline with multi 'sequence' elements");
					}
				}
				IomObject sequence=obj.getattrobj("sequence",sequencei);
				for(int segmenti=0;segmenti<sequence.getattrvaluecount("segment");segmenti++){
					IomObject segment=sequence.getattrobj("segment",segmenti);
					if(segment.getobjecttag().equals("COORD")){
						// COORD
						writeCoord(segment);
					}else if(segment.getobjecttag().equals("ARC")){
						// ARC
						writeArc(segment);
					}else{
						// custom line form
						xout.writeStartElement(iliNs,segment.getobjecttag());
						writeObjAttrs(segment);
						xout.writeEndElement(/*segment*/);
					}

				}
				if(clipped){
					xout.writeEndElement(/*CLIPPED*/);
				}
			}
			xout.writeEndElement(/*POLYLINE*/);
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}
        }

        /** writes a surface value.
         */
        private void writeSurface(IomObject obj)
		throws IoxException
        {
        /*
             object: MULTISURFACE [INCOMPLETE]
               surface // if incomplete; multi surface values
                 object: SURFACE
                   boundary
                     object: BOUNDARY
                       polyline
                         object: POLYLINE

        	
	        <SURFACE>
		        <BOUNDARY>
			        <POLYLINE .../>
			        <POLYLINE .../>
		        </BOUNDARY>
		        <BOUNDARY>
			        <POLYLINE .../>
			        <POLYLINE .../>
		        </BOUNDARY>
	        </SURFACE>
        */
		try{
			xout.writeStartElement(iliNs,"SURFACE");
			boolean clipped=obj.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			for(int surfacei=0;surfacei<obj.getattrvaluecount("surface");surfacei++){
				if(clipped){
					xout.writeStartElement(iliNs,"CLIPPED");
				}else{
					// an unclipped surface should have only one surface element
					if(surfacei>0){
						throw new IllegalArgumentException("unclipped surface with multi 'surface' elements");
					}
				}
				IomObject surface=obj.getattrobj("surface",surfacei);
				for(int boundaryi=0;boundaryi<surface.getattrvaluecount("boundary");boundaryi++){
					IomObject boundary=surface.getattrobj("boundary",boundaryi);
					xout.writeStartElement(iliNs,"BOUNDARY");
					for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
						IomObject polyline=boundary.getattrobj("polyline",polylinei);
						writePolyline(polyline,true);
					}
					xout.writeEndElement(/*BOUNDARY*/);
				}
				if(clipped){
					xout.writeEndElement(/*CLIPPED*/);
				}
			}
			xout.writeEndElement(/*SURFACE*/);
		}catch(XMLStreamException ex){
			throw new IoxException(ex);
		}
        }

        private void writeElementStringOptional(String name, String value)
		throws IoxException
        {
			try{
				if(value!=null){
					if(value.length()>0){
						xout.writeStartElement(iliNs,name);
						xout.writeCharacters(value);
						xout.writeEndElement();
					}
				}
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        private void writeAttributeStringOptional(String name, String value)
		throws IoxException
        {
			try{
				if (value != null)
				{
					String v = value.trim();
					if (v.length() > 0)
					{
						xout.writeAttribute(name, v);
					}
				}
			}catch(XMLStreamException ex){
				throw new IoxException(ex);
			}
        }
        /** gets the xml representaion of a consistency value.
         */
        private static String encodeConsistency(int consistency)
        {
	        String ret=null;
	        switch(consistency){
                case IomConstants.IOM_INCOMPLETE:
                ret="INCOMPLETE";
		        break;
            case IomConstants.IOM_INCONSISTENT:
                ret="INCONSISTENT";
		        break;
            case IomConstants.IOM_ADAPTED:
                ret="ADAPTED";
		        break;
            case IomConstants.IOM_COMPLETE:
	        default:
                ret=null;
		        break;
	        }
	        return ret;
        }

        /** gets the xml representaion of a basket-kind value.
         */
        private static String encodeBasketKind(int kind)
        {
	        String ret=null;
	        switch(kind){
	        case IomConstants.IOM_UPDATE:
                ret="UPDATE";
		        break;
            case IomConstants.IOM_INITIAL:
                ret="INITIAL";
		        break;
            case IomConstants.IOM_FULL:
	        default:
                ret=null;
		        break;
	        }
	        return ret;
        }

        /** gets the xml representaion of a operation value.
         */
        private static String encodeOperation(int ops)
        {
	        String ret=null;
	        switch(ops){
                case IomConstants.IOM_OP_UPDATE:
                ret="UPDATE";
		        break;
            case IomConstants.IOM_OP_DELETE:
                ret="DELETE";
		        break;
            case IomConstants.IOM_OP_INSERT:
	        default:
                ret=null;
		        break;
	        }
	        return ret;
        }
        private String makeOid(String value)
        {
            if (value != null)
            {
                String v = value.trim();
                if (v.length() > 0)
                {
                	if(isIli22){
                        return "x" + v;
                	}
            		return v;
                }
            }
            return null;
        }
    }
