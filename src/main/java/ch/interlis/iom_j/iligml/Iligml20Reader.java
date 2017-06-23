package ch.interlis.iom_j.iligml;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxSyntaxException;
import ch.interlis.iox_j.ObjectEvent;

public class Iligml20Reader implements IoxReader {
    private java.io.InputStream inputFile=null;
    private javax.xml.stream.XMLEventReader reader=null;
    private IoxFactoryCollection factory=new ch.interlis.iox_j.DefaultIoxFactoryCollection();
    // events
    private int state = START;
    private static final int START=0;
    private static final int START_DOCUMENT=1;
    private static final int INSIDE_TRANSFER=2;
    private static final int INSIDE_BASKETS=3;
    private static final int INSIDE_BASKET=4;
    private static final int INSIDE_MEMBER=5;
    private static final int END_OBJECT=6;
    private static final int END_MEMBER=7;
    private static final int END_BASKET=8;
    private static final int END_BASKETS=9;
    private static final int END_TRANSFER=10;
    private static final int END_DOCUMENT=11;
    // namespace
    private static final String NAMESPACE_INTERLIS="http://www.interlis.ch/ILIGML-2.0/INTERLIS";
    private static final String NAMESPACE_GML="http://www.opengis.net/gml/3.2";
    private static final String NAMESPACE_XLINK="http://www.w3.org/1999/xlink";
    private static final String NAMESPACE_XSI="http://www.w3.org/2001/XMLSchema-instance";
    // local name
    private static final String TRANSFER="TRANSFER";
    private static final String BASKETS="baskets";
    private static final String MEMBER="member";
    private static final String POLYGON="Polygon";
    private static final String CURVE="Curve";
    private static final String LINESTRING="LineString";
    private static final String ORIENTABLECURVE="OrientableCurve";
    private static final String COMPOSITECURVE="CompositeCurve";
    private static final String POINT="Point";
    private static final String POSLIST="posList";
    private static final String SEGMENTS="segments";
    private static final String ARC="Arc";
    private static final String LINESTRINGSEGMENT="LineStringSegment";
    private static final String INTERIOR="interior";
    private static final String EXTERIOR="exterior";
    private static final String CURVEMEMBER="curveMember";
    private static final String RING="Ring";
    // qnames
    private static final QName QNAME_TRANSFER=new QName(NAMESPACE_INTERLIS,TRANSFER);
    private static final QName QNAME_POLYGON=new QName(NAMESPACE_GML,POLYGON);
    private static final QName QNAME_EXTERIOR=new QName(NAMESPACE_GML,EXTERIOR);
    private static final QName QNAME_INTERIOR=new QName(NAMESPACE_GML,INTERIOR);
    private static final QName QNAME_RING=new QName(NAMESPACE_GML,RING);
    private static final QName QNAME_CURVEMEMBER=new QName(NAMESPACE_GML,CURVEMEMBER);
    private static final QName QNAME_POINT=new QName(NAMESPACE_GML,POINT);
    private static final QName QNAME_SEGMENTS=new QName(NAMESPACE_GML,SEGMENTS);
    private static final QName QNAME_POS=new QName(NAMESPACE_GML,POSLIST);
    private static final QName QNAME_BASKETS=new QName(NAMESPACE_INTERLIS,BASKETS);
    private static final QName QNAME_MEMBER=new QName(NAMESPACE_GML,MEMBER);
    private static final QName QNAME_ARC=new QName(NAMESPACE_GML,ARC);
    private static final QName QNAME_LINESTRINGSEGMENT=new QName(NAMESPACE_GML,LINESTRINGSEGMENT);
    // qnames line forms
    private static final QName QNAME_CURVE=new QName(NAMESPACE_GML,CURVE);
    private static final QName QNAME_LINESTRING=new QName(NAMESPACE_GML,LINESTRING);
    private static final QName QNAME_ORIENTABLECURVE=new QName(NAMESPACE_GML,ORIENTABLECURVE);
    private static final QName QNAME_COMPOSITECURVE=new QName(NAMESPACE_GML,COMPOSITECURVE);
    // interpolationtype of curve
    private static final String INTERPOLATIION_LINEAR="linear";
    private static final String INTERPOLATIION_CIRCULARARC3POINTS="circularArc3Points";
//    private static final String INTERPOLATIION_GEODESIC="geodesic";
//    private static final String INTERPOLATIION_CIRCULARARC2POINTSWITHBULGE="circularArc2PointWithBulge";
//    private static final String INTERPOLATIION_CIRCULARARCCENTERPOINTWITHRADIUS="circularArcCenterPointWithRadius";
//    private static final String INTERPOLATIION_ELLIPTICAL="elliptical";
//    private static final String INTERPOLATIION_CLOTHOID="clothoid";
//    private static final String INTERPOLATIION_CONIC="conic";
//    private static final String INTERPOLATIION_POLYNOMIALSPLINE="polynomialSpline";
//    private static final String INTERPOLATIION_CUBICSPLINE="cubicSpline";
//    private static final String INTERPOLATIION_RATIONALSPLINE="rationalSpline";

    public Iligml20Reader(java.io.InputStream in)throws IoxException{
        init(in);
    }

    /** Creates a new reader.
     * @param in Input reader to read from
     * @throws IoxException
     */
    public Iligml20Reader(java.io.InputStreamReader in)
    throws IoxException
    {
    }

    /** Creates a new reader.
     * @param xtffile File to read from
     * @throws IoxException
     */
    public Iligml20Reader(java.io.File xtffile)
    throws IoxException
    {
        try{
            inputFile=new java.io.FileInputStream(xtffile);
            init(inputFile);
        }catch(java.io.IOException ex){
            throw new IoxException(ex);
        }
    }

    private void init(java.io.InputStream in)
        throws IoxException
    {
        javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
        try{
            reader=inputFactory.createXMLEventReader(in);
        }catch(javax.xml.stream.XMLStreamException ex){
            throw new IoxException(ex);
        }
    }

    @Override
    public void close() throws IoxException {
        reader=null;
        if(inputFile!=null){
            try{
                inputFile.close();
            }catch(java.io.IOException ex){
                throw new IoxException(ex);
            }
            inputFile=null;
        }
    }

    @Override
    public IoxEvent read() throws IoxException {
        IomObject iomObj=null;
        while(reader.hasNext()){
            javax.xml.stream.events.XMLEvent event=null;
            try{
                event=reader.nextEvent();
            }catch(javax.xml.stream.XMLStreamException ex){
                throw new IoxException(ex);
            }
            //EhiLogger.debug(event.toString());
            // start
            if(state==START){
                if(event.isStartDocument()){
                    state=START_DOCUMENT;
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // start document
            }else if(state==START_DOCUMENT){
                if(event.isEndDocument()){
                    state=END_DOCUMENT;
                }else if(event.isStartElement()){
                    StartElement element = (StartElement) event;
                    if(element.getName().equals(QNAME_TRANSFER)){
                        state=INSIDE_TRANSFER;
                        return new ch.interlis.iox_j.StartTransferEvent();
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // start transfer
            }else if(state==INSIDE_TRANSFER){
                if(event.isEndElement()){
                    state=END_TRANSFER;
                    return new ch.interlis.iox_j.EndTransferEvent();
                }else if(event.isStartElement()){
                    state=INSIDE_BASKETS;
                    continue;
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
            // start basket
            }else if(state==INSIDE_BASKETS){
                if(event.isEndElement()){
                    state=END_BASKETS;
                }else if(event.isStartElement()){
                    StartElement element = (StartElement) event;
                    String type=getIliScopedName(element.getName());
                    if(type==null){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    QName qName = new QName(NAMESPACE_GML, "id");
                    Attribute bid = event.asStartElement().getAttributeByName(qName);
                    if(bid!=null){
                        state=INSIDE_BASKET;
                        return new ch.interlis.iox_j.StartBasketEvent(type, bid.getValue());
                    }
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // start before member
            }else if(state==INSIDE_BASKET){
                if(event.isEndElement()){
                    state=END_BASKET;
                    return new ch.interlis.iox_j.EndBasketEvent();
                }else if(event.isStartElement()){
                    state=INSIDE_MEMBER;
                    continue;
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // start class
            }else if(state==INSIDE_MEMBER){
                if(event.isEndElement()){
                    if(iomObj==null){
                        state=END_OBJECT; 
                    }else{
                        state=END_OBJECT; // </Class>
                        // return object
                        return new ch.interlis.iox_j.ObjectEvent(iomObj);
                    }
                }else if(event.isStartElement()){
                    StartElement element = (StartElement) event;
                    String type=getIliScopedName(element.getName());
                    if(type==null){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    Integer srsDimension=null;
                    QName qName = new QName(NAMESPACE_GML, "id");
                    Attribute oid = event.asStartElement().getAttributeByName(qName);
                    srsDimension=srsDimensionValidation(srsDimension, event);
                    if(oid!=null){
                    		iomObj=createIomObject(getIliScopedName(element.getName()), oid.getValue());
                        try {
                            iomObj=readObject(event, iomObj, srsDimension);
                        } catch (XMLStreamException ex) {
                        	throw new IoxException(ex);
                        }
                    }
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // end object
            }else if(state==END_OBJECT){
                if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isEndElement()){
                    state=END_MEMBER;
                    continue;
                }else if(event.isStartElement()){
                    state=INSIDE_MEMBER;
                    continue;
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // end member
            }else if(state==END_MEMBER){
                if(event.isStartElement()){
                    state=INSIDE_MEMBER;
                    continue;
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isEndElement()){
                    state=END_BASKET;
                    return new ch.interlis.iox_j.EndBasketEvent();
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // end basket
            }else if(state==END_BASKET){
                if(event.isStartElement()){
                    state=INSIDE_BASKETS;
                    continue;
                }else if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isEndElement()){
                    state=END_TRANSFER;
                    continue;
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            // end transfer
            }else if(state==END_TRANSFER){
                if(event.isCharacters()){
                    Characters characters = (Characters) event;
                    if(!characters.isWhiteSpace()){
                    	 throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.isEndElement()){
                    // end transfer event
                    state=END_DOCUMENT;
                    return new ch.interlis.iox_j.EndTransferEvent();
                }else if(event.isEndDocument()){
                    // end transfer event
                    state=END_DOCUMENT;
                    return new ch.interlis.iox_j.EndTransferEvent();
                }else{
                	throw new IoxSyntaxException(event2msgtext(event));
                }
            }
        }
        return null;
    }

    private Integer srsDimensionValidation(Integer srsDimension, XMLEvent event){
    	QName srsName=new QName("srsDimension");
    	if(srsDimension==null){
    		StartElement element = (StartElement) event;
    		if((element.getAttributeByName(srsName))!=null){
    			Integer dimensionValue=Integer.parseInt(event.asStartElement().getAttributeByName(srsName).getValue());
    			return dimensionValue;
    		}else{
    			return null;
    		}
    	}else{
    		return srsDimension;
    	}
    }
    
    private IomObject readObject(XMLEvent event, IomObject iomObj, Integer srsDimension) throws IoxException, XMLStreamException {
        String attrName=null;
        while(reader.hasNext()){
            try{
                event=reader.nextEvent(); // <class>
                event=skipSpaces(event);
            }catch(javax.xml.stream.XMLStreamException ex){
                throw new IoxException(ex);
            }
            if(attrName!=null){
                throw new IllegalStateException("expected: null, found: "+attrName);
            }
            if(event.isStartElement()){
                StartElement element = (StartElement) event;
                IomObject subIomObj=createIomObject(getIliScopedName(element.getName()), null);
                attrName=((StartElement) event).getName().getLocalPart(); // <attrName>
                srsDimension=srsDimensionValidation(srsDimension, event);
                event=reader.nextEvent(); // <class> next
                XMLEvent primAttrEvent = reader.peek(); // show next event
                if(primAttrEvent.isStartElement()){
                    event=skipSpaces(event);
                }
                if(event.isCharacters()){ // primitive attribute
            		Characters character = (Characters) event;
                    String characterValue=character.getData();
            		iomObj.setattrvalue(attrName, characterValue);
            		event=reader.nextEvent(); // <characters>
            		if(event.isEndElement()){ // check if end attribute
            			attrName=null;
            		}else{
            			throw new IoxSyntaxException(event2msgtext(event));
            		}
                }else if(event.asStartElement().getName().equals(QNAME_POINT)){
                	srsDimension=srsDimensionValidation(srsDimension, event);
            		event=reader.nextEvent(); // <Point>
                    event=skipSpaces(event);
                    if(!event.isStartElement()){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
					iomObj.addattrobj(attrName, prepareSegments(event, srsDimension));
                    event=reader.nextEvent(); // <posList>
                    event=skipSpaces(event);
                    if(event.isEndElement()){
                        attrName=null;
                        return iomObj;
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.asStartElement().getName().equals(QNAME_CURVE)){
                    srsDimension=srsDimensionValidation(srsDimension, event);
                    IomObject polyline = readCurve(event, srsDimension);
                    iomObj.addattrobj(attrName, polyline);
                    event=reader.nextEvent(); // <Curve>
                    event=skipSpaces(event);
                    if(event.isEndElement()){ // </attribute>
                        attrName=null;
                        return iomObj;
                    }else{
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                }else if(event.asStartElement().getName().equals(QNAME_LINESTRING)
                		|| event.asStartElement().getName().equals(QNAME_ORIENTABLECURVE)
                		|| event.asStartElement().getName().equals(QNAME_COMPOSITECURVE)){
                	throw new IoxSyntaxException("unsupported geometry "+event.asStartElement().getName().getLocalPart());
                }else if(event.asStartElement().getName().equals(QNAME_POLYGON)){
                	srsDimension=srsDimensionValidation(srsDimension, event);
                    // surface/area MULTISURFACE
                    iomObj.addattrobj(attrName, readPolygon(event, subIomObj, srsDimension));
                    if(!event.isStartElement()){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    event=reader.nextEvent(); // <Polygon>
                    event=skipSpaces(event);
                    return iomObj;
                }else{
                    // structure
                	String subObjName=((StartElement) event).getName().getLocalPart(); // <structure>
                	QName subQName=new QName(subObjName);
                	IomObject structObj=createIomObject(getIliScopedName(subQName), null);
                    iomObj.addattrobj(attrName, readObject(event, structObj, srsDimension));
                    if(!event.isStartElement()){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    event=reader.nextEvent(); // <structure>
                    event=skipSpaces(event);
                    if(!event.isEndElement()){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    event=reader.nextEvent(); // </structure>
                    event=skipSpaces(event);
                    if(!event.isEndElement()){
                    	throw new IoxSyntaxException(event2msgtext(event));
                    }
                    return iomObj;
                }
            }else if(event.isEndElement() && attrName==null){
                return iomObj;
            }else if(event.isEndElement() && attrName!=null){
                event=skipSpaces(event);
                if(!event.isEndElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent(); //</attribute>
                attrName=null;
            }else{
            	throw new IoxSyntaxException(event2msgtext(event));
            }
        }
        return null;
    }

	private XMLEvent skipSpaces(XMLEvent event) throws XMLStreamException, IoxSyntaxException {
        while(event.isCharacters()){
            Characters characters = (Characters) event;
            if(!characters.isWhiteSpace()){
				throw new IoxSyntaxException(event2msgtext(event));
            }
            event=reader.nextEvent();
        }
        return event;
    }

    private IomObject readCurve(XMLEvent event, Integer srsDimension) throws IoxException, XMLStreamException {
    	srsDimension=srsDimensionValidation(srsDimension, event);
        if(!event.asStartElement().getName().equals(QNAME_CURVE)){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // <Curve>
        event=skipSpaces(event);
        if(!event.asStartElement().getName().equals(QNAME_SEGMENTS)){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        IomObject polylineObj=createIomObject("POLYLINE", null);
        if(!event.isStartElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // <segments>
        event=skipSpaces(event);
        QName interpolationType=new QName("interpolation");
        String interpolationValue="";
        // linestringsegment
        while(event.isStartElement()){
            StartElement element = (StartElement) event;
            // interpolationType
            if(element.getAttributeByName(interpolationType)!=null){
            	interpolationValue = element.getAttributeByName(interpolationType).getValue();
            }
            if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_LINEAR)){
            	srsDimension=srsDimensionValidation(srsDimension, event);
            	if(!event.isStartElement()){
            		throw new IoxSyntaxException(event2msgtext(event));
                }
            	// straights
        		event=reader.nextEvent(); // <linear>
                event=skipSpaces(event);
            	polylineObj.addattrobj("sequence", prepareSegments(event, srsDimension));
            	if(!event.isStartElement()){
            		throw new IoxSyntaxException(event2msgtext(event));
                }
            	event=reader.nextEvent(); // <posList>
                event=skipSpaces(event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_GEODESIC)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CIRCULARARC3POINTS)){
            	srsDimension=srsDimensionValidation(srsDimension, event);
                // arcs
                polylineObj.addattrobj("sequence", readArcSegment(event, srsDimension));
                if(!event.isStartElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent(); // <segments>/<linestring>
                event=skipSpaces(event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CIRCULARARC2POINTSWITHBULGE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CIRCULARARCCENTERPOINTWITHRADIUS)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_ELLIPTICAL)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CLOTHOID)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CONIC)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_POLYNOMIALSPLINE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_CUBICSPLINE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
//            }else if(interpolationValue!=null && interpolationValue.equals(INTERPOLATIION_RATIONALSPLINE)){
//            	srsDimension=srsDimensionValidation(srsDimension, event);
            }else{
            	throw new IoxSyntaxException("unsupported interpolation "+interpolationValue);
            }
            if(event.isEndElement()){
	            event=reader.nextEvent(); // </segments>
	            event=skipSpaces(event);
            }else{
            	// ignore event
            }
        }
        return polylineObj;
    }

	private IomObject prepareSegments(XMLEvent event, Integer srsDimension) throws IoxException, XMLStreamException {
		IomObject segments=createIomObject("SEGMENTS", null);
        if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POS)){
        	srsDimension=srsDimensionValidation(srsDimension, event);
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            } 
        	event=reader.nextEvent(); // <posList>
            if(event.isCharacters()){
            	if(srsDimension==null){
            		throw new IoxSyntaxException("syntax error no srsDimension defined");
            	}
	            Characters posData = (Characters) event;
                // dimension C1,C2,C3
                String[] linearSegments = posData.getData().split(" ");
                for(int i=0;i<linearSegments.length;i++){
                	ArrayList<String>segmentList=new ArrayList<String>();
                	if(srsDimension.equals(1)){
                		if((i+2)>linearSegments.length){
                			 throw new IoxSyntaxException("missing coord values for 1d Coord");
                		}
                		segmentList.add(linearSegments[i]);
            			IomObject linearSegment=readLineSegment(segmentList);
            			segments.addattrobj("segment", linearSegment);
                	}else if(srsDimension.equals(2)){
                		if((i+2)>linearSegments.length){
               			 	throw new IoxSyntaxException("missing coord values for 2d Coord");
                		}
                		segmentList.add(linearSegments[i]);
                		i=i+1;
                		segmentList.add(linearSegments[i]);
            			IomObject linearSegment=readLineSegment(segmentList);
            			segments.addattrobj("segment", linearSegment);
                	}else if(srsDimension.equals(3)){
                		if((i+3)>linearSegments.length){
               			 	throw new IoxSyntaxException("missing coord values for 3d Coord");
                		}
                		segmentList.add(linearSegments[i]);
                		i=i+1;
                		segmentList.add(linearSegments[i]);
                		i=i+1;
                		segmentList.add(linearSegments[i]);
            			IomObject linearSegment=readLineSegment(segmentList);
            			segments.addattrobj("segment", linearSegment);
                	}else{
                		throw new IoxSyntaxException(event2msgtext(event));
                    }
                }
                event=reader.nextEvent(); // <characters>
                if(!event.isEndElement()){
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent(); // </posList>
                event=skipSpaces(event);
                return segments;
            }
        }
		return null;
	}

	private IomObject readArcSegment(XMLEvent event, Integer srsDimension) throws IoxException, XMLStreamException{
		srsDimension=srsDimensionValidation(srsDimension, event);
		if(!event.isStartElement()){
			throw new IoxSyntaxException(event2msgtext(event));
        }
		event=reader.nextEvent(); // circular3PointArc
        event=skipSpaces(event);
        if(event.isStartElement() && event.asStartElement().getName().equals(QNAME_POS)){ // posList
        	srsDimension=srsDimensionValidation(srsDimension, event);
        	IomObject arcObj=createIomObject("ARC", null);
            event=reader.nextEvent();
            if(event.isCharacters()){
            	if(srsDimension==null){
            		throw new IoxSyntaxException("no srsDimension defined");
            	}
	            Characters posData = (Characters) event;
                // dimension C1,C2,C3,A1,A2
                String[] arcPositions = posData.getData().split(" "); // if whitespace >1 will be trimmed in further methode
                if(arcPositions.length<4){
                    throw new IoxSyntaxException("missing arc pos values");
                }
                if(arcPositions.length>0){
                    arcObj.setattrvalue("A1", arcPositions[0]);
                }
                if(arcPositions.length>1){
                    arcObj.setattrvalue("A2", arcPositions[1]);
                }
                if(arcPositions.length>2){
                    arcObj.setattrvalue("C1", arcPositions[2]);
                }
                if(arcPositions.length>3){
                    arcObj.setattrvalue("C2", arcPositions[3]);
                }
                if(arcPositions.length>4){
                    arcObj.setattrvalue("C3", arcPositions[4]);
                }
                if(arcPositions.length>5){
                    throw new IoxSyntaxException("too many arc pos values");
                }
                event=reader.nextEvent();
                if(!event.isEndElement()){  // </pos>
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                event=reader.nextEvent();
                event=skipSpaces(event);
                if(!event.isEndElement()){ // </Point>
                	throw new IoxSyntaxException(event2msgtext(event));
                }
                return arcObj;
            }else{
            	throw new IoxSyntaxException(event2msgtext(event));
            }
        }else if(event.isEndElement()){
            throw new IoxSyntaxException("missing arc pos element");
        }else{
        	throw new IoxSyntaxException(event2msgtext(event));
        }
	}

	private IomObject readLineSegment(ArrayList<String> coords) throws IoxException{
		IomObject coordObj=createIomObject("COORD", null);
		for(String coord:coords){
			coord=coord.trim();
		}
		if(coords.size()<1){
            throw new IoxSyntaxException("missing coord pos values");
        }
        if(coords.size()>0){
        	coordObj.setattrvalue("C1", coords.get(0));
        }
        if(coords.size()>1){
        	coordObj.setattrvalue("C2", coords.get(1));
        }
        if(coords.size()>2){
        	coordObj.setattrvalue("C3", coords.get(2));
        }
        if(coords.size()>5){
        	throw new IoxSyntaxException("too many coord pos values");
        }
		return coordObj;
	}

	private IomObject readPolygon(XMLEvent event, IomObject iomObj, Integer srsDimension) throws IoxException, XMLStreamException { // attrName, MULTISIRFACE
    	IomObject boundaryObj=createIomObject("BOUNDARY", null);
    	IomObject surfaceObj=createIomObject("SURFACE", null);
    	IomObject polygonObj=createIomObject("MULTISURFACE", null);
    	if(!event.isStartElement()){
    		throw new IoxSyntaxException(event2msgtext(event));
        }
    	event=reader.nextEvent(); // <polygon>
        event=skipSpaces(event);
        if(!event.isStartElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        int exteriorExist=0; // first boundary has to be an OuterBoundary (exterior)
        while(event.isStartElement() && (event.asStartElement().getName().equals(QNAME_EXTERIOR) || event.asStartElement().getName().equals(QNAME_INTERIOR))){
        	if(event.asStartElement().getName().equals(QNAME_EXTERIOR)){
        		exteriorExist=exteriorExist+1;
        	}else if(event.asStartElement().getName().equals(QNAME_INTERIOR)){
        		if(exteriorExist==0){
            		throw new IoxSyntaxException("no exterior ring found");
            	}
        	}
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <exterior/interior>
            event=skipSpaces(event);
            
        	boundaryObj=readBoundary(event, surfaceObj, srsDimension);
        	surfaceObj.addattrobj("boundary", boundaryObj);
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <Ring>
        	event=skipSpaces(event);
        }
        if(exteriorExist==0){
        	throw new IoxSyntaxException("no lines found in polygon of "+iomObj.getobjecttag());
        }
        polygonObj.addattrobj("surface", surfaceObj);
        return polygonObj;
    }

	private String event2msgtext(XMLEvent event){
		String output="";
		if(event instanceof StartElement){
			 output="unexpected start element "+event.asStartElement().getName().getLocalPart();
		}else if(event instanceof EndElement){
			output="unexpected end element "+event.asEndElement().getName().getLocalPart();
		}else if(event instanceof Characters){
			output="unexpected characters "+((Characters) event).getData();
		}
		return output;
	}
	
    private IomObject readBoundary(XMLEvent event, IomObject surfaceObj, Integer srsDimension) throws IoxException, XMLStreamException {
        IomObject boundaryObj=createIomObject("BOUNDARY", null);
        if(!event.asStartElement().getName().equals(QNAME_RING)){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // <Ring>
        event=skipSpaces(event);
        while(event.isStartElement() && event.asStartElement().getName().equals(QNAME_CURVEMEMBER)){
        	srsDimension=srsDimensionValidation(srsDimension, event);
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <curveMember>
            event=skipSpaces(event);
        	boundaryObj.addattrobj("polyline", readCurve(event, srsDimension));
        	if(!event.isStartElement()){
        		throw new IoxSyntaxException(event2msgtext(event));
            }
        	event=reader.nextEvent(); // <Curve>
            event=skipSpaces(event);
        }
        if(!event.isEndElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // </curveMember>
        event=skipSpaces(event);
        if(!event.isEndElement()){
        	throw new IoxSyntaxException(event2msgtext(event));
        }
        event=reader.nextEvent(); // </Ring>
        event=skipSpaces(event);
		return boundaryObj;
	}

	@Override
    public IomObject createIomObject(String type, String oid)
            throws IoxException {
        return factory.createIomObject(type, oid);
    }
    @Override
    public IoxFactoryCollection getFactory() throws IoxException {
        return factory;
    }
    @Override
    public void setFactory(IoxFactoryCollection factory) throws IoxException {
        this.factory=factory;
    }
    private void ObjectComposition(StartElement element) {
        // ObjectComposition
    }
    private String getIliScopedName(QName qName) {
        return qName.getLocalPart();
    }
}