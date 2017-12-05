package ch.interlis.iom_j.iligml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlunit.xpath.JAXPXPathEngine;
import org.xmlunit.xpath.XPathEngine;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Iligml20WriterTest {

	private final static String TEST_OUT="build";
	private final static String TEST_IN="src/test/data/Iligml20Writer";
	private TransferDescription td=null;
	@BeforeClass
	public static void setupEnv()
	{
		new File(TEST_OUT).mkdir();
	}
	private void addArc(IomObject polyline,double xa, double ya,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("ARC",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		ret.setattrvalue("A1", Double.toString(xa));
		ret.setattrvalue("A2", Double.toString(ya));
		sequence.addattrobj("segment",ret);
	}
	private void addCoord(IomObject polyline,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("COORD",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		sequence.addattrobj("segment",ret);
	}
	private IomObject newPolyline()
	{
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("POLYLINE",null);
		IomObject sequence=new ch.interlis.iom_j.Iom_jObject("SEGMENTS",null);
		ret.addattrobj("sequence",sequence);
		return ret;
	}
	private IomObject createPolgon(IomObject polyline) {
		// MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("MULTISURFACE",null);
		IomObject surface=new ch.interlis.iom_j.Iom_jObject("SURFACE",null);
		IomObject boundary=new ch.interlis.iom_j.Iom_jObject("BOUNDARY",null);
		boundary.addattrobj("polyline", polyline);
		surface.addattrobj("boundary",boundary);
		ret.addattrobj("surface",surface);
		return ret;
	}
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/Iligml20Writer/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	@Test
	public void testEmptyTransfer() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"EmptyTransfer-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testEmptyBasket() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"EmptyBasket-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicF","bid1"));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testEmptyObjects() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"EmptyObjects-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicF","bid1"));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicF.TableF0","10")));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicF.TableF1","20")));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicF.TableF0","11")));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicF.TableF1","21")));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testUndefinedSurface() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"UndefinedSurface-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicA","bid1"));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicA.TableA","10")));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicA.TableA","11")));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testSurface() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"Surface-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicA","bid1"));
		IomObject iomObj=new Iom_jObject("Test1.TopicA.TableA","10");
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addArc(polyline,115.0,  108.0,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject polygon=createPolgon(polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		iomObj=new Iom_jObject("Test1.TopicA.TableA","11");
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		polygon=createPolgon(polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testUndefinedArea() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"UndefinedArea-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicB","bid1"));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicB.TableB","10")));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicB.TableB","11")));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testArea() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"Area-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicB","bid1"));
		IomObject iomObj=new Iom_jObject("Test1.TopicB.TableB","10");
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addArc(polyline,115.0,  108.0,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject polygon=createPolgon(polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		iomObj=new Iom_jObject("Test1.TopicB.TableB","11");
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		polygon=createPolgon(polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testUndefinedReference() throws Iox2jtsException, IoxException {
		Iligml20Writer writer=new Iligml20Writer(new File(TEST_OUT,"UndefinedReference-out.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicG","bid1"));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicG.ClassG1","10")));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicG.ClassG3","20")));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testReference() throws Iox2jtsException, IoxException, ParserConfigurationException, SAXException, IOException {
		File destFile = new File(TEST_OUT,"Reference-out.gml");
		Iligml20Writer writer=new Iligml20Writer(destFile,td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicG","bid1"));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicG.ClassG1","10")));
		writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicG.ClassG1","11")));
		Iom_jObject objG2_20=new Iom_jObject("Test1.TopicG.ClassG2","20");
		objG2_20.addattrobj("attrRefG1", "REF").setobjectrefoid("10");
		writer.write(new ObjectEvent(objG2_20));
		Iom_jObject objG2_21=new Iom_jObject("Test1.TopicG.ClassG2","21");
		objG2_21.addattrobj("attrRefG1", "REF").setobjectrefoid("10");
		writer.write(new ObjectEvent(objG2_21));
		Iom_jObject objG3=new Iom_jObject("Test1.TopicG.ClassG3","30");
		objG3.addattrobj("attrRefG1", "REF").setobjectrefoid("10");
		writer.write(new ObjectEvent(objG3));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
	    DocumentBuilder docBuilder = dbf.newDocumentBuilder();
	    Document doc = docBuilder.parse(destFile);
	    doc.normalizeDocument();
	    File controlFile=new File(TEST_IN,"Reference.gml");
	    Document controlDoc = docBuilder.parse(controlFile);
	    controlDoc.normalizeDocument();
	    XPathEngine xpath = new JAXPXPathEngine();
	    java.util.HashMap<String,String> nsMap=new java.util.HashMap<String,String>();
	    nsMap.put("gml", "http://www.opengis.net/gml/3.2");
	    nsMap.put("xtf", "http://www.interlis.ch/ILIGML-2.0/Test1");
	    xpath.setNamespaceContext(nsMap);
	    assertThat(xpath.selectNodes("//*[@gml:id='x10']", doc).iterator().next(),org.xmlunit.matchers.CompareMatcher.isSimilarTo(xpath.selectNodes("//*[@gml:id='x10']", controlDoc).iterator().next()).ignoreWhitespace().ignoreComments().throwComparisonFailure());
	    assertThat(xpath.selectNodes("//*[@gml:id='x11']", doc).iterator().next(),org.xmlunit.matchers.CompareMatcher.isSimilarTo(xpath.selectNodes("//*[@gml:id='x11']", controlDoc).iterator().next()).ignoreWhitespace().ignoreComments().throwComparisonFailure());
	    assertThat(xpath.selectNodes("//*[@gml:id='x20']", doc).iterator().next(),org.xmlunit.matchers.CompareMatcher.isSimilarTo(xpath.selectNodes("//*[@gml:id='x20']", controlDoc).iterator().next()).ignoreWhitespace().ignoreComments().throwComparisonFailure());
	    assertThat(xpath.selectNodes("//*[@gml:id='x21']", doc).iterator().next(),org.xmlunit.matchers.CompareMatcher.isSimilarTo(xpath.selectNodes("//*[@gml:id='x21']", controlDoc).iterator().next()).ignoreWhitespace().ignoreComments().throwComparisonFailure());
	    assertThat(xpath.selectNodes("//*[@gml:id='x30']", doc).iterator().next(),org.xmlunit.matchers.CompareMatcher.isSimilarTo(xpath.selectNodes("//*[@gml:id='x30']", controlDoc).iterator().next()).ignoreWhitespace().ignoreComments().throwComparisonFailure());
	}

	
}
