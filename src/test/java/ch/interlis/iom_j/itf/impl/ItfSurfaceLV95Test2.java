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
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.LogCollector;

public class ItfSurfaceLV95Test2 {

	private TransferDescription td=null;
	private Table tableA=null;
	private AttributeDef formAttr=null;
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/Itf/Test2LV95.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
		tableA=(Table) ((Container<Element>) ((Container<Element>) td.getElement(Model.class, "Test2LV95")).getElement(Topic.class, "TopicA")).getElement(Table.class, "TableA");
		assertNotNull(tableA);
		formAttr=(AttributeDef) tableA.getElement(AttributeDef.class, "Form");
		assertNotNull(formAttr);
	}
	

	@Test
	public void testOverhangNoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableA.getScopedName(null);
		String formAttrTableName=tableA.getContainer().getScopedName(null)+"."+tableA.getName()+"_"+formAttr.getName();
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95Surface1.itf"));
		reader.setModel(td);
		EhiLogger.getInstance().setTraceFilter(false);
		IoxEvent event=null;
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		        	if(iomObj.getobjecttag().equals(formAttrTableName)){
		        		builder.addItfLinetableObject(iomObj);
		        	}else if(iomObj.getobjecttag().equals(tableBName)){
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("38");
		//System.out.println(polygon.toString());
		//assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}"
		//		,polygon.toString());
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		errFactory.setLogger(logger);
		builder.validatePolygon("38", formAttr, polygon, errFactory, null);
		assertEquals(0,logger.getErrs().size());
	}
	@Test
	public void testEye() throws Iox2jtsException, IoxException {
		String tableBName=tableA.getScopedName(null);
		String formAttrTableName=tableA.getContainer().getScopedName(null)+"."+tableA.getName()+"_"+formAttr.getName();
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95Eye.itf"));
		reader.setModel(td);
		EhiLogger.getInstance().setTraceFilter(false);
		IoxEvent event=null;
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		        	if(iomObj.getobjecttag().equals(formAttrTableName)){
		        		builder.addItfLinetableObject(iomObj);
		        	}else if(iomObj.getobjecttag().equals(tableBName)){
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("72000368");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2612099.122, C2 1225200.173}, COORD {C1 2612108.466, C2 1225201.431}, ARC {A1 2612104.262, A2 1225197.329, C1 2612099.122, C2 1225200.173}]}}}}}"
				,polygon.toString());
	}
	
}
