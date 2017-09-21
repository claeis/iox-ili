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
import ch.interlis.iom_j.itf.impl.ItfAreaLinetable2Polygon;
import ch.interlis.iox.*;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class ItfAreaLV95Test {

	private TransferDescription td=null;
	private Table tableB=null;
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
		tableB=(Table) ((Container<Element>) ((Container<Element>) td.getElement(Model.class, "Test2LV95")).getElement(Topic.class, "TopicB")).getElement(Table.class, "TableB");
		assertNotNull(tableB);
		formAttr=(AttributeDef) tableB.getElement(AttributeDef.class, "Form");
		assertNotNull(formAttr);
	}
	
	@Test
	public void testShape1_aP() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape1_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}",polygon.toString());
	}

	@Test
	public void testShape1_bP() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape1_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}

	@Test
	public void testShape1_cP() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape1_NoOverlap.itf"));
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
					builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
				}
			}else if(event instanceof EndBasketEvent){
			}else if(event instanceof EndTransferEvent){
			}
		}while(!(event instanceof EndTransferEvent));
		
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_aP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_bP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 26, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1135000.0, A1 2710000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_cP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 25, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_aP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_bP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2690000.0, C2 1065000.0}, COORD {C1 2674999.95, C2 1130000.0}, ARC {A2 1145000.0, A1 2674999.95, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testShape2_cP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_aP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 250, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_bP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 266, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1155000.0, A1 2690000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testShape2_cP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 250, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testShape2_OverlapFail() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape2_Overlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		try {	
			builder.buildSurfaces();
			fail();
		}catch(Exception ex) {
			assertTrue(ex.getMessage().contains("intersections"));		
		}
	}
	
	@Test
	public void testTwoArcs_Shape3_aP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_bP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon);
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1135000.0, A1 2710000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_cP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_aP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_bP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1155000.0, A1 2690000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testTwoArcs_Shape3_cP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_aP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_bP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1155000.0, A1 2690000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testTwoArcs_Shape3_cP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_OverlapFail() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape3_Overlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		try {	
			builder.buildSurfaces();
			fail();
		}catch(Exception ex) {
			assertTrue(ex.getMessage().contains("intersections"));		
		}
	}
	
	@Test
	public void testTwoArcs_Shape4_aP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, ARC {A2 1120000.0, A1 2632500.0, C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_bP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2640000.0, C2 1134000.0}, ARC {A2 1135000.0, A1 2660000.0, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2700000.0, C2 1065000.0}, COORD {C1 2670000.0, C2 1075000.0}, COORD {C1 2640000.0, C2 1134000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_cP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_aP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_bP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1155000.0, A1 2690000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testTwoArcs_Shape4_cP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_aP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_bP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1155000.0, A1 2690000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testTwoArcs_Shape4_cP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_OverlapFail() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape4_Overlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		try {	
			builder.buildSurfaces();
			fail();
		}catch(Exception ex) {
			assertTrue(ex.getMessage().contains("intersections"));		
		}
	}
	
	@Test
	public void testTwoArcs_Shape5_aP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, ARC {A2 1120000.0, A1 2632500.0, C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_bP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1096000.0}, ARC {A2 1135000.0, A1 2641000.0, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2700000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1096000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_cP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_aP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, ARC {A2 1120000.0, A1 2632500.0, C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_bP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1075000.0}, ARC {A2 1125000.0, A1 2636000.0, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2700000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1075000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testTwoArcs_Shape5_cP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_aP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_bP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1155000.0, A1 2690000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testTwoArcs_Shape5_cP_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_OverlapOk.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_OverlapFail() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape5_Overlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		try {	
			builder.buildSurfaces();
			fail();
		}catch(Exception ex) {
			assertTrue(ex.getMessage().contains("intersections"));		
		}
	}
	
	@Test
	public void testTwoArcs_Shape6_aP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape6_bP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1135000.0, A1 2710000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape6_cP_NoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_NoOverlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape6_aP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("aP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape6_bP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("bP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2740000.0, C2 1065000.0}, ARC {A2 1155000.0, A1 2690000.0, C1 2660000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testTwoArcs_Shape6_cP_OnLine() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_OnLine.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
			
		builder.buildSurfaces();
		IomObject polygon=builder.getSurfaceObject("cP");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testTwoArcs_Shape6_OverlapFail() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_Overlap.itf"));
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
		        		builder.addGeoRef(iomObj.getobjectoid(), iomObj.getattrobj(formAttr.getName(), 0));
		        	}
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		try {	
			builder.buildSurfaces();
			fail();
		}catch(Exception ex) {
			assertTrue(ex.getMessage().contains("intersections"));		
		}
	}
}