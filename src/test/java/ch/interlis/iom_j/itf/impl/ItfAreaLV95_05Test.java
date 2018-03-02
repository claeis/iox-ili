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
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class ItfAreaLV95_05Test {

	private TransferDescription td=null;
	private Table tableB=null;
	private AttributeDef formAttr=null;
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/Itf/Test2LV95_05.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
		tableB=(Table) ((Container<Element>) ((Container<Element>) td.getElement(Model.class, "Test2LV95_05")).getElement(Topic.class, "TopicB")).getElement(Table.class, "TableB");
		assertNotNull(tableB);
		formAttr=(AttributeDef) tableB.getElement(AttributeDef.class, "Form");
		assertNotNull(formAttr);
	}
	
	@Test
	public void testOverhangOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95_05AreaOverhangOverlap.itf"));
		reader.setModel(td);
		//EhiLogger.getInstance().setTraceFilter(false);
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
		IomObject polygon=builder.getSurfaceObject("14");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2610432.457, C2 1182691.551}, ARC {A1 2610433.547, A2 1182693.794, C1 2610434.152, C2 1182696.214}, ARC {A1 2610434.047045499, A2 1182695.5981813655, C1 2610433.910314906, C2 1182694.9886300697}, ARC {A1 2610433.901, A2 1182694.959, C1 2610433.382, C2 1182693.788}, COORD {C1 2610432.457, C2 1182691.551}]}}}}}"
				,polygon.toString());
	}

	@Test
	public void testOverhangNoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95_05AreaOverhangNoOverlap.itf"));
		reader.setModel(td);
		//EhiLogger.getInstance().setTraceFilter(false);
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
		IomObject polygon=builder.getSurfaceObject("14");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2610432.457, C2 1182691.551}, ARC {A1 2610433.547, A2 1182693.794, C1 2610434.152, C2 1182696.214}, ARC {A1 2610434.047045499, A2 1182695.5981813655, C1 2610433.910314906, C2 1182694.9886300697}, ARC {A1 2610433.901, A2 1182694.959, C1 2610433.382, C2 1182693.788}, COORD {C1 2610432.457, C2 1182691.551}]}}}}}"
				,polygon.toString());
	}
	
	@Test
	public void testAcceptHoles() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95_05AreaHole.itf"));
		reader.setModel(td);
		//EhiLogger.getInstance().setTraceFilter(false);
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
		IomObject polygon=builder.getSurfaceObject("10");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2610400.0, C2 1182600.0}, COORD {C1 2610400.0, C2 1182680.0}, COORD {C1 2610480.0, C2 1182680.0}, COORD {C1 2610480.0, C2 1182600.0}, COORD {C1 2610400.0, C2 1182600.0}]}}}, "+
		"BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2610420.0, C2 1182620.0}, COORD {C1 2610460.0, C2 1182620.0}, COORD {C1 2610460.0, C2 1182660.0}, COORD {C1 2610420.0, C2 1182660.0}, COORD {C1 2610420.0, C2 1182620.0}]}}}]}}"
				,polygon.toString());
	}
	@Test
	public void testRejectHole() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		builder.setAllowItfAreaHoles(false); // override default
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95_05AreaHole.itf"));
		reader.setModel(td);
		//EhiLogger.getInstance().setTraceFilter(false);
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
		} catch (IoxInvalidDataException e) {
			assertEquals("Test2LV95_05.TopicB.TableB.Form: no area-ref to polygon of lines hole",e.getMessage());
		}
		IomObject polygon=builder.getSurfaceObject("10");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2610400.0, C2 1182600.0}, COORD {C1 2610400.0, C2 1182680.0}, COORD {C1 2610480.0, C2 1182680.0}, COORD {C1 2610480.0, C2 1182600.0}, COORD {C1 2610400.0, C2 1182600.0}]}}}, "+
		"BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2610420.0, C2 1182620.0}, COORD {C1 2610460.0, C2 1182620.0}, COORD {C1 2610460.0, C2 1182660.0}, COORD {C1 2610420.0, C2 1182660.0}, COORD {C1 2610420.0, C2 1182620.0}]}}}]}}"
				,polygon.toString());
	}
	@Test
	public void testStraightArcWithSharpAngle() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/TestLV95_05StraightArcWithSharpAngle.itf"));
		reader.setModel(td);
		//EhiLogger.getInstance().setTraceFilter(false);
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
		IomObject polygon=builder.getSurfaceObject("1");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2597049.929, C2 1199888.336}, ARC {A1 2597050.06, A2 1199888.284, C1 2597050.195, C2 1199888.326}, COORD {C1 2597053.018, C2 1199885.644}, COORD {C1 2597049.929, C2 1199888.336}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testStraightArcArcWithSharpAngleNoOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/TestLV95_05StraightArcArcWithSharpAngleNoOverlap.itf"));
		reader.setModel(td);
		//EhiLogger.getInstance().setTraceFilter(false);
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
		IomObject polygon=builder.getSurfaceObject("3");
		//System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2601728.773, C2 1200134.762}, COORD {C1 2601729.515, C2 1200132.921}, ARC {A1 2601729.979, A2 1200131.034, C1 2601731.105, C2 1200129.45}, ARC {A1 2601729.404, A2 1200131.874, C1 2601728.773, C2 1200134.762}]}}}}}"
				,polygon.toString());
	}
	@Test
	public void testStraightArcArcWithSharpAngleOverlap() throws Iox2jtsException, IoxException {
		String tableBName=tableB.getScopedName(null);
		String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
		ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
		ItfReader reader=new ItfReader(new File("src/test/data/Itf/TestLV95_05StraightArcArcWithSharpAngleOverlap.itf"));
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
		IomObject polygon=builder.getSurfaceObject("3");
		System.out.println(polygon.toString());
		assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2601728.773, C2 1200134.762}, COORD {C1 2601729.515, C2 1200132.921}, ARC {A1 2601729.966, A2 1200131.028, C1 2601730.9743354, C2 1200129.5762131875}, COORD {C1 2601731.105, C2 1200129.45}, COORD {C1 2601730.972208524, C2 1200129.5754712452}, ARC {A1 2601729.383, A2 1200131.862, C1 2601728.773, C2 1200134.762}]}}}}}"
				,polygon.toString());
	}
    @Test
    public void testStraightArcSmallRadius() throws Iox2jtsException, IoxException {
        String tableBName=tableB.getScopedName(null);
        String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
        ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
        ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95_05StraightArcSmallRadius.itf"));
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
        IomObject polygon=builder.getSurfaceObject("1");
        //System.out.println(polygon.toString());
        assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2667159.447, C2 1208679.449}, COORD {C1 2667160.843, C2 1208679.318}, ARC {A1 2667160.84, A2 1208679.317, C1 2667160.837, C2 1208679.315}, COORD {C1 2667159.447, C2 1208679.449}]}}}}}"
              ,polygon.toString());
    }
    @Test
    public void testStraightArcShortArc() throws Iox2jtsException, IoxException {
        String tableBName=tableB.getScopedName(null);
        String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
        ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
        ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95_05StraightArcShortArc.itf"));
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
        IomObject polygon=builder.getSurfaceObject("2");
        //System.out.println(polygon.toString());
        assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2666358.233, C2 1209946.127}, COORD {C1 2666358.596, C2 1209946.192}, ARC {A1 2666358.593, A2 1209946.184, C1 2666358.589, C2 1209946.177}, COORD {C1 2666358.233, C2 1209946.127}]}}}}}"
              ,polygon.toString());
    }
    @Test
    public void testHorizontalStraight() throws Iox2jtsException, IoxException {
        String tableBName=tableB.getScopedName(null);
        String formAttrTableName=tableB.getContainer().getScopedName(null)+"."+tableB.getName()+"_"+formAttr.getName();
        ItfAreaLinetable2Polygon builder=new ItfAreaLinetable2Polygon(formAttr,false);
        ItfReader reader=new ItfReader(new File("src/test/data/Itf/Test2LV95_05HorizontalStraight.itf"));
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
        IomObject polygon=builder.getSurfaceObject("6690");
        //System.out.println(polygon.toString());
        assertEquals("MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 2642808.915, C2 1167440.934}, COORD {C1 2642815.216, C2 1167464.737}, COORD {C1 2642822.716, C2 1167478.436}, COORD {C1 2642836.415, C2 1167555.94}, COORD {C1 2642838.915, C2 1167555.94}, COORD {C1 2642837.716, C2 1167550.94}, COORD {C1 2642847.716, C2 1167535.939}, COORD {C1 2642832.716, C2 1167483.436}, COORD {C1 2642830.216, C2 1167462.236}, COORD {C1 2642823.915, C2 1167448.435}, COORD {C1 2642821.415, C2 1167448.435}, COORD {C1 2642817.016, C2 1167428.634}, COORD {C1 2642813.915, C2 1167425.933}, COORD {C1 2642808.915, C2 1167440.934}]}}}}}"
              ,polygon.toString());
    }
}
