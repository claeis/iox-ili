package ch.interlis.iom_j.iligml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

public class IligmlWriterTest {

	private static String TEST_OUT="build";
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
		FileEntry fileEntry=new FileEntry("src/test/data/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	@Test
	public void testF1() throws Iox2jtsException, IoxException {
		IligmlWriter writer=new IligmlWriter(new File(TEST_OUT,"TestF1.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testF3() throws Iox2jtsException, IoxException {
		IligmlWriter writer=new IligmlWriter(new File(TEST_OUT,"TestF3.gml"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicF","bid1"));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testF4() throws Iox2jtsException, IoxException {
		IligmlWriter writer=new IligmlWriter(new File(TEST_OUT,"TestF4.gml"),td);
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
	public void testA1() throws Iox2jtsException, IoxException {
		IligmlWriter writer=new IligmlWriter(new File(TEST_OUT,"TestA1.gml"),td);
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
	public void testA2() throws Iox2jtsException, IoxException {
		IligmlWriter writer=new IligmlWriter(new File(TEST_OUT,"TestA2.gml"),td);
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
	public void testB1() throws Iox2jtsException, IoxException {
		IligmlWriter writer=new IligmlWriter(new File(TEST_OUT,"TestB1.gml"),td);
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
	public void testB2() throws Iox2jtsException, IoxException {
		IligmlWriter writer=new IligmlWriter(new File(TEST_OUT,"TestB2.gml"),td);
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

	
}
