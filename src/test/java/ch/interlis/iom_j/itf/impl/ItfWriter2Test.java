package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.itf.ItfWriter2;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class ItfWriter2Test {
	private final static String TEST_OUT="build";

	private TransferDescription td=null;
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
	private IomObject newPolygon() {
		// MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("MULTISURFACE",null);
		IomObject surface=new ch.interlis.iom_j.Iom_jObject("SURFACE",null);
		ret.addattrobj("surface",surface);
		return ret;
	}
	private void addBoundary(IomObject polygon,IomObject polyline){
		IomObject boundary=new ch.interlis.iom_j.Iom_jObject("BOUNDARY",null);
		boundary.addattrobj("polyline", polyline);
		IomObject surface=polygon.getattrobj("surface",0);
		surface.addattrobj("boundary",boundary);
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
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestF1.itf"),td);
		writer.write(new StartTransferEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testF3() throws Iox2jtsException, IoxException {
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestF3.itf"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicF","bid1"));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testF4() throws Iox2jtsException, IoxException {
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestF4.itf"),td);
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
	public void testSurfaceNull() throws Iox2jtsException, IoxException {
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestSurfaceNull.itf"),td);
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
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestSurfaceWithArc.itf"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicA","bid1"));
		IomObject iomObj=new Iom_jObject("Test1.TopicA.TableA","10");
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addArc(polyline,115.0,  108.0,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject polygon=newPolygon();
		addBoundary(polygon,polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		iomObj=new Iom_jObject("Test1.TopicA.TableA","11");
		polyline=newPolyline();
		addCoord(polyline,110.0,  110.0);
		addCoord(polyline,115.0,  115.0); 
		addCoord(polyline,115.0,  120.0); 
		addCoord(polyline,112.0,  120.0); 
		addCoord(polyline,110.0,  110.0);
		polygon=newPolygon();
		addBoundary(polygon,polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testAreaEmpty() throws Iox2jtsException, IoxException {
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestAreaEmpty.itf"),td);
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
	public void testAreaSimple() throws Iox2jtsException, IoxException {
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestAreaSimple.itf"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicB","bid1"));
		IomObject iomObj=new Iom_jObject("Test1.TopicB.TableB","10");
		IomObject polygon=newPolygon();
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addCoord(polyline,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		addBoundary(polygon, polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testAreaWithArc() throws Iox2jtsException, IoxException {
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestAreaWithArc.itf"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicB","bid1"));
		IomObject iomObj=new Iom_jObject("Test1.TopicB.TableB","10");
		IomObject polyline=newPolyline();
		addCoord(polyline,110,110);
		addArc(polyline,115.0,  108.0,120.0,  110.0); 
		addCoord(polyline,120.0,  140.0); 
		addCoord(polyline,110.0,  140.0); 
		addCoord(polyline,110.0,  110.0);
		IomObject polygon=newPolygon();
		addBoundary(polygon,polyline);
		iomObj.addattrobj("Form", polygon);
		writer.write(new ObjectEvent(iomObj));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}
	@Test
	public void testAreaWithHole() throws Iox2jtsException, IoxException {
		ItfWriter2 writer=new ItfWriter2(new File(TEST_OUT,"TestAreaWithHole.itf"),td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicB","bid1"));
		IomObject hole=null;
		{
            IomObject polygon=newPolygon();
		    {
	            IomObject shell=newPolyline();
	            addCoord(shell,110.0,  110.0);
	            addCoord(shell,120.0,  110.0); 
	            addCoord(shell,120.0,  140.0); 
	            addCoord(shell,110.0,  140.0); 
	            addCoord(shell,110.0,  110.0);
	            addBoundary(polygon,shell);
		    }
		    {
	            hole=newPolyline();
	            addCoord(hole,110.0,  110.0);
	            addCoord(hole,115.0,  115.0); 
	            addCoord(hole,115.0,  120.0); 
	            addCoord(hole,112.0,  120.0); 
	            addCoord(hole,110.0,  110.0);
	            addBoundary(polygon,hole);
		    }
	        IomObject iomObj=new Iom_jObject("Test1.TopicB.TableB","10");
	        iomObj.addattrobj("Form", polygon);
	        writer.write(new ObjectEvent(iomObj));
		}
		{
	        IomObject iomObj=new Iom_jObject("Test1.TopicB.TableB","11");
	        IomObject polygon=newPolygon();
	        addBoundary(polygon,hole);
	        iomObj.addattrobj("Form", polygon);
	        writer.write(new ObjectEvent(iomObj));
		    
		}
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
	}

	
}
