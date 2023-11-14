package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;

import java.io.File;

import ch.interlis.ili2c.metamodel.AbstractCoordType;
import ch.interlis.ili2c.metamodel.Domain;
import org.junit.Before;
import org.junit.Ignore;
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

public class ItfSurfaceLV95_05Test {

	private TransferDescription td=null;
	private Table tableA=null;
	private AttributeDef formAttr=null;
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/itf/Test2LV95_05.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
		tableA=(Table) ((Container<Element>) ((Container<Element>) td.getElement(Model.class, "Test2LV95_05")).getElement(Topic.class, "TopicA")).getElement(Table.class, "TableA");
		assertNotNull(tableA);
		formAttr=(AttributeDef) tableA.getElement(AttributeDef.class, "Form");
		assertNotNull(formAttr);
	}
	

	@Test
	@Ignore("#89")
	public void testSmallVertexDistance() throws Iox2jtsException, IoxException {
	    // the minimal distance of a segement endpoint to the other segment is
	    // test ok with newVertexOffset=0.001*Math.sqrt(2.0)/2.0;
	    // test fails with current code because it assumes newVertexOffset=0.001*2.0;
		String tableBName=tableA.getScopedName(null);
		String formAttrTableName=tableA.getContainer().getScopedName(null)+"."+tableA.getName()+"_"+formAttr.getName();
		ItfSurfaceLinetable2Polygon builder=new ItfSurfaceLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2SurfaceSmallVertexDistance.itf"));
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
		IomObject polygon=builder.getSurfaceObject("2515");
		//System.out.println(polygon.toString());
		//assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}"
		//		,polygon.toString());
		LogCollector logger=new LogCollector();
		LogEventFactory errFactory=new LogEventFactory();
		errFactory.setLogger(logger);
		Domain coordDomain = (Domain) td.getElement("Test2LV95_05.LKoord");
		builder.validatePolygon("2515", formAttr, polygon, errFactory, null, (AbstractCoordType) coordDomain.getType());
		assertEquals(0,logger.getErrs().size());
	}	
}
