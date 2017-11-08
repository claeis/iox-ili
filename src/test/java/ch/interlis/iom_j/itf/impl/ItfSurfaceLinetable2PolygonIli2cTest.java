package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.ItfReader;
import ch.interlis.iox.*;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class ItfSurfaceLinetable2PolygonIli2cTest {

	private TransferDescription td=null;
	private Table tableA=null;
	private AttributeDef formAttr=null;
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
		tableA=(Table) ((Container<Element>) ((Container<Element>) td.getElement(Model.class, "Test1")).getElement(Topic.class, "TopicA")).getElement(Table.class, "TableA");
		assertNotNull(tableA);
		formAttr=(AttributeDef) tableA.getElement(AttributeDef.class, "Form");
		assertNotNull(formAttr);
	}
	
	@Test
	public void test1() throws Iox2jtsException, IoxException {
		String tableAName=tableA.getScopedName(null);
		String formAttrTableName=tableA.getContainer().getScopedName(null)+"."+tableA.getName()+"_"+formAttr.getName();
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test1.itf"));
		reader.setModel(td);
		IoxEvent event=null;
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		        	if(iomObj.getobjecttag().equals(formAttrTableName)){
		        		builder.addItfLinetableObject(iomObj);
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("10");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void test1b() throws Iox2jtsException, IoxException {
		String tableAName=tableA.getScopedName(null);
		String formAttrTableName=tableA.getContainer().getScopedName(null)+"."+tableA.getName()+"_"+formAttr.getName();
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test1b.itf"));
		reader.setModel(td);
		IoxEvent event=null;
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		        	if(iomObj.getobjecttag().equals(formAttrTableName)){
		        		builder.addItfLinetableObject(iomObj);
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("10");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, ARC {A1 115.0, A2 108.0, C1 110.0, C2 110.0}]}}}}}"
				,polygon.toString());
	}

}
