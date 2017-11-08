package ch.interlis.iom_j.itf.impl;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

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
import ch.interlis.iom_j.itf.impl.jtsext.algorithm.CurveSegmentIntersector;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;
import ch.interlis.iox.*;
import ch.interlis.iox_j.jts.Iox2jtsException;

@Ignore("review of tests required")
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
	public void testTwoArcs_Shape1_NoOverlap() throws Iox2jtsException, IoxException {
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
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1065000.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape2_NoOverlap() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100000.0}, COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500002.5, C2 1100000.0}, COORD {C1 2500000.0, C2 1100000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500005.0, C2 1100000.0}, COORD {C1 2500003.5, C2 1100001.6}, ARC {A2 1100001.69, A1 2500002.98, C1 2500002.5, C2 1100002.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500000.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500000.0, C2 1100002.0}]}}}}}"
				,polygon3.toString());	
	}
	
	@Test // TODO isValid(): invalid CompoundCurve: Intersection overlap null
	public void testTwoArcs_Shape2_OverlapOk() throws Iox2jtsException, IoxException {
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
		 	assertEquals(""
				,polygon.toString());
		 	IomObject polygon2=builder.getSurfaceObject("bP");
		 	assertEquals(""
				,polygon2.toString());
		 	IomObject polygon3=builder.getSurfaceObject("cP");
		 	assertEquals(""
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape2_OnLine() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100000.0}, COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500002.5, C2 1100000.0}, COORD {C1 2500000.0, C2 1100000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500005.0, C2 1100000.0}, COORD {C1 2500004.5, C2 1100000.0}, ARC {A2 1100000.59, A1 2500003.09, C1 2500002.5, C2 1100002.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500000.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500000.0, C2 1100002.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape2_OverlapFail() throws Iox2jtsException, IoxException {
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
	public void testTwoArcs_Shape3_NoOverlap() throws Iox2jtsException, IoxException {
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
		IomObject polygon1=builder.getSurfaceObject("aP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, ARC {A2 1099552.934, A1 2581254.832, C1 2651446.292, C2 1017163.56}, COORD {C1 2600000.0, C2 930000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon1.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2620511.425, C2 1064528.486}, ARC {A2 1098706.773, A1 2618923.742, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2620511.425, C2 1064528.486}]}}}}}"
				,polygon2.toString());
		builder.buildSurfaces();
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon3.toString());
		
	}
	
	@Test
	public void testTwoArcs_Shape3_OnLine() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, ARC {A2 1125513.424, A1 2624167.256, C1 2640454.248, C2 1016476.557}, COORD {C1 2600000.0, C2 930000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2620511.425, C2 1064528.486}, ARC {A2 1098706.773, A1 2618923.742, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2620511.425, C2 1064528.486}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape3_OverlapOk() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, ARC {A2 1174999.979, A1 2659999.786, C1 2651446.292, C2 1017163.56}, COORD {C1 2600000.0, C2 930000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2620511.425, C2 1064528.486}, ARC {A2 1174999.978, A1 2659999.787, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2620511.425, C2 1064528.486}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon3.toString());
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
	
	@Test // Info: removeOverlap(): valoverlap Intersection overlap 6.253970524117136E-5
	public void testTwoArcs_Shape4_NoOverlap() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100000.0}, COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, ARC {A2 1100000.58, A1 2500001.91, C1 2500000.5, C2 1100000.0}, COORD {C1 2500000.0, C2 1100000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500005.0, C2 1100000.0}, COORD {C1 2500004.5, C2 1100000.0}, ARC {A2 1100000.58, A1 2500003.08, C1 2500002.500686828, C2 1100001.9247877654}, ARC {A2 1100001.962397116, A1 2500002.500697495, C1 2500002.5, C2 1100002.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500000.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500000.0, C2 1100002.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape4_OverlapOk() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100000.0}, COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, ARC {A2 1100001.7564875996, A1 2500002.5139487144, C1 2500002.5574254855, C2 1100001.5164821285}, ARC {A2 1100000.7, A1 2500002.307, C1 2500000.5, C2 1100000.0}, COORD {C1 2500000.0, C2 1100000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500005.0, C2 1100000.0}, COORD {C1 2500004.5, C2 1100000.0}, ARC {A2 1100000.586, A1 2500003.086, C1 2500002.5, C2 1100002.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500000.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500000.0, C2 1100002.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test // Info: removeOverlap(): valoverlap Intersection overlap 2.4589326394153233E-5
	public void testTwoArcs_Shape4_OnLine() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100000.0}, COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, ARC {A2 1100000.575, A1 2500001.9, C1 2500000.5, C2 1100000.0}, COORD {C1 2500000.0, C2 1100000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500005.0, C2 1100000.0}, COORD {C1 2500004.5, C2 1100000.0}, ARC {A2 1100000.58, A1 2500003.08, C1 2500002.5005608117, C2 1100001.929464307}, ARC {A2 1100001.964734619, A1 2500002.5005905125, C1 2500002.5, C2 1100002.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500000.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500000.0, C2 1100002.0}]}}}}}"
				,polygon3.toString());
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
	public void testTwoArcs_Shape5_NoOverlap() throws Iox2jtsException, IoxException {
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
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1096000.0}, ARC {A2 1135000.0, A1 2641000.0, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2700000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1096000.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_OnLine() throws Iox2jtsException, IoxException {
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
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2660000.0, C2 1075000.0}, ARC {A2 1125000.0, A1 2636000.0, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2700000.0, C2 1065000.0}, COORD {C1 2660000.0, C2 1075000.0}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape5_OverlapOk_removeOverlap() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, ARC {A2 1120000.0, A1 2632500.0, C1 2660000.0, C2 1065000.0}, COORD {C1 2500000.0, C2 1065000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2637708.523626023, C2 1093750.3713318582}, ARC {A2 1138189.9272158023, A1 2634950.008492493, C1 2660000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2830000.0, C2 1065000.0}, COORD {C1 2660000.01, C2 1065000.0}, ARC {A2 1077873.9615011232, A1 2646918.0678773983, C1 2637708.523626023, C2 1093750.3713318582}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1285000.0}, COORD {C1 2830000.0, C2 1175000.0}, COORD {C1 2660000.0, C2 1175000.0}, COORD {C1 2500000.0, C2 1175000.0}]}}}}}"
				,polygon3.toString());
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
	public void testTwoArcs_Shape6_NoOverlap() throws Iox2jtsException, IoxException {
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
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100000.0}, COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, ARC {A2 1100001.0, A1 2500001.5, C1 2500002.5, C2 1100000.0}, COORD {C1 2500000.0, C2 1100000.0}]}}}}}"
				,polygon.toString());
		IomObject polygon2=builder.getSurfaceObject("bP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500002.48, C2 1100001.2}, ARC {A2 1100001.6, A1 2500002.4, C1 2500002.5, C2 1100002.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500005.0, C2 1100000.0}, COORD {C1 2500002.48, C2 1100001.2}]}}}}}"
				,polygon2.toString());
		IomObject polygon3=builder.getSurfaceObject("cP");
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500000.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500000.0, C2 1100002.0}]}}}}}"
				,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape6_OnLine() throws Iox2jtsException, IoxException {
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
			assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100000.0}, COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, ARC {A2 1100001.0, A1 2500001.5, C1 2500002.5, C2 1100000.0}, COORD {C1 2500000.0, C2 1100000.0}]}}}}}"
					,polygon.toString());
			IomObject polygon2=builder.getSurfaceObject("bP");
			assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500002.4, C2 1100000.6}, ARC {A2 1100001.3, A1 2500001.85, C1 2500002.5, C2 1100002.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500005.0, C2 1100000.0}, COORD {C1 2500002.4, C2 1100000.6}]}}}}}"
					,polygon2.toString());
			IomObject polygon3=builder.getSurfaceObject("cP");
			assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2500000.0, C2 1100002.0}, COORD {C1 2500000.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100003.0}, COORD {C1 2500005.0, C2 1100002.0}, COORD {C1 2500002.5, C2 1100002.0}, COORD {C1 2500000.0, C2 1100002.0}]}}}}}"
					,polygon3.toString());
	}
	
	@Test
	public void testTwoArcs_Shape6_OverlapOk() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2Area_Shape6_OverlapOk.itf"));
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
//			assertEquals(""
//					,polygon.toString());
			IomObject polygon2=builder.getSurfaceObject("bP");
			System.out.println(polygon2.toString());
//			assertEquals(""
//					,polygon2.toString());
			IomObject polygon3=builder.getSurfaceObject("cP");
			System.out.println(polygon3.toString());
//			assertEquals(""
//					,polygon3.toString());
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