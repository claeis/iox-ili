package ch.interlis.iom_j.itf;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Ignore;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iox.*;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox_j.logging.LogEventImpl;

public class ItfReader2Test {

	private TransferDescription td=null;
	private LogCollector errs=null;
	private void assertError(String expected,String tid)
	{
		LogEvent foundErr=null;
		for(LogEvent err:errs.getErrs()){
			if(expected.equals(err.getEventMsg())){
				foundErr=err;
				break;
			}
			if(err.getException()!=null && expected.equals(err.getException().getMessage())){
				foundErr=err;
				break;
			}
		}
		if(foundErr!=null && foundErr instanceof LogEventImpl){
			if(tid!=null && tid.equals(((LogEventImpl) foundErr).getSourceObjectXtfId())){
				return;
			}
		}
		throw new ComparisonFailure("no such error",expected,"");
		
	}
	private void assertError(String expected)
	{
		LogEvent foundErr=null;
		for(LogEvent err:errs.getErrs()){
			if(expected.equals(err.getEventMsg())){
				foundErr=err;
				break;
			}
			if(err.getException()!=null && expected.equals(err.getException().getMessage())){
				foundErr=err;
				break;
			}
		}
		if(foundErr!=null){
			return;
		}
		throw new ComparisonFailure("no such error",expected,"");
		
	}
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("src/test/data/ItfReader2/Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
        fileEntry=new FileEntry("src/test/data/ItfReader2/Test2.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
		errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
	}
    @Test
    @Ignore
    public void testUserData() throws Exception {
        EhiLogger.getInstance().setTraceFilter(false);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("User.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        ili2cConfig.setGenerateWarnings(false);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
        
        ItfReader2 reader=new ItfReader2(new File("User.itf"),false);
        reader.setModel(td);
        reader.setReadLinetables(true);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                }else if(event instanceof ObjectEvent){
                    IomObject iomObj=((ObjectEvent)event).getIomObject();
                    assertNotNull(iomObj.getobjectoid());
                    objs.put(iomObj.getobjectoid(), iomObj);
                }else if(event instanceof EndBasketEvent){
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));
    }
	
    @Test
    public void testSURFACEbasic() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceBasic.itf"),false);
        reader.setModel(td);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                }else if(event instanceof ObjectEvent){
                    IomObject iomObj=((ObjectEvent)event).getIomObject();
                    System.out.println(iomObj);
                    assertNotNull(iomObj.getobjectoid());
                    objs.put(iomObj.getobjectoid(), iomObj);
                }else if(event instanceof EndBasketEvent){
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));
         assertEquals("Test1.TopicA.TableA oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}}", 
                 objs.get("10").toString());
    }
    @Test
    public void testSURFACEbasicFilter() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceBasic.itf"),false);
        reader.setModel(td);
        reader.setTopicFilter(new String[] {"Test1.TopicB"});
        IoxEvent event=null;
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                    fail();
                }else if(event instanceof ObjectEvent){
                    fail();
                }else if(event instanceof EndBasketEvent){
                    fail();
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));
    }
    @Test
    public void testSURFACEwithLinetables() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/Surface2Basic.itf"),false);
        reader.setModel(td);
        reader.setReadLinetables(true);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                }else if(event instanceof ObjectEvent){
                    IomObject iomObj=((ObjectEvent)event).getIomObject();
                    assertNotNull(iomObj.getobjectoid());
                    objs.put(iomObj.getobjectoid(), iomObj);
                }else if(event instanceof EndBasketEvent){
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));
         assertEquals(3,objs.size());
         assertEquals("Test2.TopicA.TableA oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}}", 
                 objs.get("10").toString());
         assertEquals("Test2.TopicA.TableA_Form oid 1 {Linienart eins, _itf_geom_TableA POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}]}}, _itf_ref_TableA -> 10 REF {}}", 
                 objs.get("1").toString());
         assertEquals("Test2.TopicA.TableA_Form oid 2 {Linienart zwei, _itf_geom_TableA POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}]}}, _itf_ref_TableA -> 10 REF {}}", 
                 objs.get("2").toString());
    }
    @Test
    public void testSURFACEintersection() throws Iox2jtsException, IoxException {
        EhiLogger.getInstance().setTraceFilter(false);
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceIntersection.itf"),false);
        reader.setModel(td);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         try {
             do{
                 event=reader.read();
                 if(event instanceof StartTransferEvent){
                 }else if(event instanceof StartBasketEvent){
                 }else if(event instanceof ObjectEvent){
                     IomObject iomObj=((ObjectEvent)event).getIomObject();
                     //System.out.println(iomObj);
                     assertNotNull(iomObj.getobjectoid());
                     objs.put(iomObj.getobjectoid(), iomObj);
                 }else if(event instanceof EndBasketEvent){
                 }else if(event instanceof EndTransferEvent){
                 }
             }while(!(event instanceof EndTransferEvent));
             fail();
        }catch(IoxInvalidDataException ex){
            assertEquals("failed to build polygons",ex.getLocalizedMessage());
            assertError("failed to build polygons of Test1.TopicA.TableA.Form");
            assertError("Intersection coord1 (686638.497, 152674.248), tids 3218, 3218");
             // verify that the valid geometry value is read
             assertEquals("Test1.TopicA.TableA oid VeritiID34395 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 687644.618, C2 154306.589}, COORD {C1 687656.999, C2 154312.905}, COORD {C1 687662.675, C2 154315.8}, COORD {C1 687669.036, C2 154306.543}, COORD {C1 687659.196, C2 154301.648}, COORD {C1 687647.743, C2 154295.95}, COORD {C1 687644.618, C2 154306.589}]}}}}}}", 
                     objs.get("VeritiID34395").toString());
        }
         // Test1.TopicA.TableA oid VeritiID34395 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 687644.618, C2 154306.589}, COORD {C1 687656.999, C2 154312.905}, COORD {C1 687662.675, C2 154315.8}, COORD {C1 687669.036, C2 154306.543}, COORD {C1 687659.196, C2 154301.648}, COORD {C1 687647.743, C2 154295.95}, COORD {C1 687644.618, C2 154306.589}]}}}}}}
    }
	@Test
	public void testSURFACEundefined() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceUndefined.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		    		System.out.println(iomObj);
		    		assertNotNull(iomObj.getobjectoid());
		    		objs.put(iomObj.getobjectoid(), iomObj);
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		 assertEquals("Test1.TopicA.TableA oid 10 {}", 
				 objs.get("10").toString());
	}
	@Test
	public void testSURFACEemptyline() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceEmptyLine.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		try{
			 do{
			        event=reader.read();
			        if(event instanceof StartTransferEvent){
			        }else if(event instanceof StartBasketEvent){
			        }else if(event instanceof ObjectEvent){
			        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			    		System.out.println(iomObj);
			    		assertNotNull(iomObj.getobjectoid());
			    		objs.put(iomObj.getobjectoid(), iomObj);
			        }else if(event instanceof EndBasketEvent){
			        }else if(event instanceof EndTransferEvent){
			        }
			 }while(!(event instanceof EndTransferEvent));
			 fail();
		}catch(IoxInvalidDataException ex){
			 assertError("empty line","1");
		}
	}
	@Test
	public void testSURFACEnoMainObj() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceNoMainObj.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		try{
			 do{
			        event=reader.read();
			        if(event instanceof StartTransferEvent){
			        }else if(event instanceof StartBasketEvent){
			        }else if(event instanceof ObjectEvent){
			        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			    		System.out.println(iomObj);
			    		assertNotNull(iomObj.getobjectoid());
			    		objs.put(iomObj.getobjectoid(), iomObj);
			        }else if(event instanceof EndBasketEvent){
			        }else if(event instanceof EndTransferEvent){
			        }
			 }while(!(event instanceof EndTransferEvent));
			 fail();
		}catch(IoxInvalidDataException ex){
			 assertError("unexpected linetable Test1.TopicA.TableA_Form");
		}
	}
    @Test
    public void testSURFACEnoRefToMainObj() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceNoRefToMainObj.itf"),false);
        reader.setModel(td);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
        try{
             do{
                    event=reader.read();
                    if(event instanceof StartTransferEvent){
                    }else if(event instanceof StartBasketEvent){
                    }else if(event instanceof ObjectEvent){
                        IomObject iomObj=((ObjectEvent)event).getIomObject();
                        System.out.println(iomObj);
                        assertNotNull(iomObj.getobjectoid());
                        objs.put(iomObj.getobjectoid(), iomObj);
                    }else if(event instanceof EndBasketEvent){
                    }else if(event instanceof EndTransferEvent){
                    }
             }while(!(event instanceof EndTransferEvent));
             fail();
        }catch(IoxInvalidDataException ex){
             assertEquals("line 11: missing reference to maintable Test1.TopicA.TableA",ex.getMessage());
        }
    }
    @Test
    public void testSURFACEnullRefToMainObj() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceNullRefToMainObj.itf"),false);
        reader.setModel(td);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
        try{
             do{
                    event=reader.read();
                    if(event instanceof StartTransferEvent){
                    }else if(event instanceof StartBasketEvent){
                    }else if(event instanceof ObjectEvent){
                        IomObject iomObj=((ObjectEvent)event).getIomObject();
                        System.out.println(iomObj);
                        assertNotNull(iomObj.getobjectoid());
                        objs.put(iomObj.getobjectoid(), iomObj);
                    }else if(event instanceof EndBasketEvent){
                    }else if(event instanceof EndTransferEvent){
                    }
             }while(!(event instanceof EndTransferEvent));
             fail();
        }catch(IoxInvalidDataException ex){
             assertEquals("line 11: missing reference to maintable Test1.TopicA.TableA",ex.getMessage());
        }
    }
	@Test
	public void testSURFACEnoMainTable() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceNoMainTable.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		try{
			 do{
			        event=reader.read();
			        if(event instanceof StartTransferEvent){
			        }else if(event instanceof StartBasketEvent){
			        }else if(event instanceof ObjectEvent){
			        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			    		System.out.println(iomObj);
			    		assertNotNull(iomObj.getobjectoid());
			    		objs.put(iomObj.getobjectoid(), iomObj);
			        }else if(event instanceof EndBasketEvent){
			        }else if(event instanceof EndTransferEvent){
			        }
			 }while(!(event instanceof EndTransferEvent));
			 fail();
		}catch(IoxInvalidDataException ex){
			 assertError("unexpected linetable Test1.TopicA.TableA_Form");
		}
	}

	@Test
	public void testAREAbasic() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/AreaBasic.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		    		System.out.println(iomObj);
		    		assertNotNull(iomObj.getobjectoid());
		    		objs.put(iomObj.getobjectoid(), iomObj);
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));	 
		 assertEquals("Test1.TopicB.TableB oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}, _itf_Form COORD {C1 115.0, C2 115.0}}", 
				 objs.get("10").toString());
	}
    @Test
    public void testAREAbasicFilter() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/AreaBasic.itf"),false);
        reader.setModel(td);
        reader.setTopicFilter(new String[] {"Test1.TopicA"});
        IoxEvent event=null;
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                    fail();
                }else if(event instanceof ObjectEvent){
                    fail();
                }else if(event instanceof EndBasketEvent){
                    fail();
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));
    }
    @Test
    public void testAREAwithLinetables() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/Area2TwoPolygons.itf"),false);
        reader.setModel(td);
        reader.setReadLinetables(true);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                }else if(event instanceof ObjectEvent){
                    IomObject iomObj=((ObjectEvent)event).getIomObject();
                    assertNotNull(iomObj.getobjectoid());
                    objs.put(iomObj.getobjectoid(), iomObj);
                }else if(event instanceof EndBasketEvent){
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));   
         assertEquals(5,objs.size());
         assertNotNull(objs.get("1"));
         assertNotNull(objs.get("2"));
         assertNotNull(objs.get("3"));
         assertNotNull(objs.get("10"));
         assertNotNull(objs.get("11"));
         assertEquals("Test2.TopicB.TableB_Form oid 1 {Linienart eins, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}, COORD {C1 120.0, C2 110.0}]}}, _itf_ref_TableB -> 10 REF {}}", 
                 objs.get("1").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 2 {Linienart zwei, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}]}}, _itf_ref2_TableB -> 11 REF {}, _itf_ref_TableB -> 10 REF {}}", 
                 objs.get("2").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 3 {Linienart drei, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 130.0, C2 110.0}, COORD {C1 130.0, C2 140.0}, COORD {C1 120.0, C2 140.0}]}}, _itf_ref_TableB -> 11 REF {}}", 
                 objs.get("3").toString());
         assertEquals("Test2.TopicB.TableB oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}, _itf_Form COORD {C1 115.0, C2 115.0}}", 
                 objs.get("10").toString());
         assertEquals("Test2.TopicB.TableB oid 11 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 130.0, C2 140.0}, COORD {C1 130.0, C2 110.0}, COORD {C1 120.0, C2 110.0}]}}}}}, _itf_Form COORD {C1 125.0, C2 115.0}}", 
                 objs.get("11").toString());
    }
    @Test
    public void testAREAwithLinetablesOverlap() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/Area2Overlap.itf"),false);
        reader.setModel(td);
        reader.setReadLinetables(true);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                }else if(event instanceof ObjectEvent){
                    IomObject iomObj=((ObjectEvent)event).getIomObject();
                    assertNotNull(iomObj.getobjectoid());
                    //System.out.println(iomObj);
                    objs.put(iomObj.getobjectoid(), iomObj);
                }else if(event instanceof EndBasketEvent){
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));   
         assertEquals(12,objs.size());
         assertEquals("Test2.TopicB.TableB_Form oid 3 {Linienart drei, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 432.457, C2 691.551}, COORD {C1 433.382, C2 693.788}]}}, _itf_ref_TableB -> 14 REF {}}", 
                 objs.get("3").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 7 {Linienart sieben, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 428.727, C2 723.456}, COORD {C1 434.352, C2 696.214}, COORD {C1 437.488, C2 678.734}]}}, _itf_ref_TableB -> 647t648 REF {}}", 
                 objs.get("7").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 1 {Linienart eins, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 434.152, C2 696.214}, ARC {A1 433.901, A2 694.959, C1 433.382, C2 693.788}]}}, _itf_ref2_TableB -> 51t647 REF {}, _itf_ref_TableB -> 14 REF {}}", 
                 objs.get("1").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 6 {Linienart sechs, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 428.727, C2 723.456}, COORD {C1 434.152, C2 696.214}]}}, _itf_ref2_TableB -> 647t648 REF {}, _itf_ref_TableB -> 51t647 REF {}}", 
                 objs.get("6").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 2 {Linienart zwei, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 432.457, C2 691.551}, ARC {A1 433.547, A2 693.794, C1 434.152, C2 696.214}]}}, _itf_ref2_TableB -> 57t648 REF {}, _itf_ref_TableB -> 14 REF {}}", 
                 objs.get("2").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 8 {Linienart acht, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 434.152, C2 696.214}, COORD {C1 437.488, C2 678.734}]}}, _itf_ref2_TableB -> 647t648 REF {}, _itf_ref_TableB -> 57t648 REF {}}", 
                 objs.get("8").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 4 {Linienart vier, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 432.457, C2 691.551}, COORD {C1 437.488, C2 678.734}]}}, _itf_ref_TableB -> 57t648 REF {}}", 
                 objs.get("4").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 5 {Linienart fuenf, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 433.382, C2 693.788}, COORD {C1 428.727, C2 723.456}]}}, _itf_ref_TableB -> 51t647 REF {}}", 
                 objs.get("5").toString());
         assertEquals("Test2.TopicB.TableB oid 14 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 432.457, C2 691.551}, ARC {A1 433.547, A2 693.794, C1 434.152, C2 696.214}, ARC {A1 434.0123978928245, A2 695.4716972679216, C1 433.8265604160284, C2 694.7395999201618}, ARC {A1 433.62572822265344, A2 694.2537800576945, C1 433.382, C2 693.788}, COORD {C1 432.457, C2 691.551}]}}}}}, _itf_Form COORD {C1 433.482, C2 693.788}}", 
                 objs.get("14").toString());
         assertEquals("Test2.TopicB.TableB oid 51t647 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 428.727, C2 723.456}, COORD {C1 434.152, C2 696.214}, ARC {A1 434.0123978928245, A2 695.4716972679216, C1 433.8265604160284, C2 694.7395999201618}, ARC {A1 433.62572822265344, A2 694.2537800576945, C1 433.382, C2 693.788}, COORD {C1 428.727, C2 723.456}]}}}}}, _itf_Form COORD {C1 433.382, C2 693.888}}", 
                 objs.get("51t647").toString());
         assertEquals("Test2.TopicB.TableB oid 57t648 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 432.457, C2 691.551}, ARC {A1 433.547, A2 693.794, C1 434.152, C2 696.214}, COORD {C1 437.488, C2 678.734}, COORD {C1 432.457, C2 691.551}]}}}}}, _itf_Form COORD {C1 432.557, C2 691.551}}", 
                 objs.get("57t648").toString());
         assertEquals("Test2.TopicB.TableB oid 647t648 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 428.727, C2 723.456}, COORD {C1 434.352, C2 696.214}, COORD {C1 437.488, C2 678.734}, COORD {C1 434.152, C2 696.214}, COORD {C1 428.727, C2 723.456}]}}}}}, _itf_Form COORD {C1 434.252, C2 696.214}}", 
                 objs.get("647t648").toString());
    }
    @Test
    public void testAREAwithLinetablesSingleLine() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/Area2SingleLine.itf"),false);
        reader.setModel(td);
        reader.setReadLinetables(true);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                }else if(event instanceof ObjectEvent){
                    IomObject iomObj=((ObjectEvent)event).getIomObject();
                    assertNotNull(iomObj.getobjectoid());
                    System.out.println(iomObj.toString());
                    objs.put(iomObj.getobjectoid(), iomObj);
                }else if(event instanceof EndBasketEvent){
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));   
         assertEquals(3,objs.size());
         assertEquals("Test2.TopicB.TableB_Form oid 1:2 {Linienart eins, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 111.0, C2 113.0}, COORD {C1 113.0, C2 111.0}, COORD {C1 110.0, C2 110.0}]}}, _itf_ref_TableB -> 10 REF {}}", 
                 objs.get("1:2").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 1:1 {Linienart eins, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}]}}, _itf_ref_TableB -> 10 REF {}}", 
                 objs.get("1:1").toString());
         assertEquals("Test2.TopicB.TableB oid 10 {Form MULTISURFACE {surface SURFACE {boundary [BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}, BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 113.0, C2 111.0}, COORD {C1 111.0, C2 113.0}, COORD {C1 110.0, C2 110.0}]}}}]}}, _itf_Form COORD {C1 115.0, C2 115.0}}", 
                 objs.get("10").toString());
    }
    @Test
    public void testAREAwithLinetablesSplitLine() throws Iox2jtsException, IoxException {
        ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/Area2TwoPolygonsSplitLine.itf"),false);
        reader.setModel(td);
        reader.setReadLinetables(true);
        IoxEvent event=null;
        HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
         do{
                event=reader.read();
                if(event instanceof StartTransferEvent){
                }else if(event instanceof StartBasketEvent){
                }else if(event instanceof ObjectEvent){
                    IomObject iomObj=((ObjectEvent)event).getIomObject();
                    assertNotNull(iomObj.getobjectoid());
                    objs.put(iomObj.getobjectoid(), iomObj);
                }else if(event instanceof EndBasketEvent){
                }else if(event instanceof EndTransferEvent){
                }
         }while(!(event instanceof EndTransferEvent));   
         assertEquals(9,objs.size());
         assertNotNull(objs.get("1:1"));
         assertEquals("Test2.TopicB.TableB_Form oid 3:2 {Linienart zwei, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 130.0, C2 140.0}, COORD {C1 120.0, C2 140.0}]}}, _itf_ref2_TableB -> 12 REF {}, _itf_ref_TableB -> 11 REF {}}", 
                 objs.get("3:2").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 3:1 {Linienart zwei, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 130.0, C2 110.0}, COORD {C1 130.0, C2 140.0}]}}, _itf_ref_TableB -> 11 REF {}}", 
                 objs.get("3:1").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 4 {Linienart drei, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 160.0}, COORD {C1 130.0, C2 160.0}, COORD {C1 130.0, C2 140.0}]}}, _itf_ref_TableB -> 12 REF {}}", 
                 objs.get("4").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 1:3 {Linienart eins, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}]}}, _itf_ref2_TableB -> 11 REF {}, _itf_ref_TableB -> 10 REF {}}", 
                 objs.get("1:3").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 1:2 {Linienart eins, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 110.0}, COORD {C1 120.0, C2 110.0}]}}, _itf_ref_TableB -> 10 REF {}}", 
                 objs.get("1:2").toString());
         assertEquals("Test2.TopicB.TableB_Form oid 1:1 {Linienart eins, _itf_geom_TableB POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}]}}, _itf_ref2_TableB -> 12 REF {}, _itf_ref_TableB -> 10 REF {}}", 
                 objs.get("1:1").toString());
         assertEquals("Test2.TopicB.TableB oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}, _itf_Form COORD {C1 115.0, C2 115.0}}", 
                 objs.get("10").toString());
         assertEquals("Test2.TopicB.TableB oid 11 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 120.0, C2 110.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 130.0, C2 140.0}, COORD {C1 130.0, C2 110.0}, COORD {C1 120.0, C2 110.0}]}}}}}, _itf_Form COORD {C1 125.0, C2 115.0}}", 
                 objs.get("11").toString());
         assertEquals("Test2.TopicB.TableB oid 12 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 140.0}, COORD {C1 110.0, C2 160.0}, COORD {C1 130.0, C2 160.0}, COORD {C1 130.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 110.0, C2 140.0}]}}}}}, _itf_Form COORD {C1 120.0, C2 150.0}}", 
                 objs.get("12").toString());
    }
	@Test
	public void testAREAundefined() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/AreaUndefined.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		    		System.out.println(iomObj);
		    		assertNotNull(iomObj.getobjectoid());
		    		objs.put(iomObj.getobjectoid(), iomObj);
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));	 
		 assertEquals("Test1.TopicB.TableB oid 10 {}", 
				 objs.get("10").toString());
	}
	@Test
	public void testAREAemptyline() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/AreaEmptyLine.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		try{
			 do{
			        event=reader.read();
			        if(event instanceof StartTransferEvent){
			        }else if(event instanceof StartBasketEvent){
			        }else if(event instanceof ObjectEvent){
			        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			    		System.out.println(iomObj);
			    		assertNotNull(iomObj.getobjectoid());
			    		objs.put(iomObj.getobjectoid(), iomObj);
			        }else if(event instanceof EndBasketEvent){
			        }else if(event instanceof EndTransferEvent){
			        }
			 }while(!(event instanceof EndTransferEvent));
			 fail();
		}catch(IoxInvalidDataException ex){
			 assertError("empty line","1");
		}
	}
	@Test
	public void testAREAnoMainObj() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/AreaNoMainObj.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		try{
			 do{
			        event=reader.read();
			        if(event instanceof StartTransferEvent){
			        }else if(event instanceof StartBasketEvent){
			        }else if(event instanceof ObjectEvent){
			        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			    		System.out.println(iomObj);
			    		assertNotNull(iomObj.getobjectoid());
			    		objs.put(iomObj.getobjectoid(), iomObj);
			        }else if(event instanceof EndBasketEvent){
			        }else if(event instanceof EndTransferEvent){
			        }
			 }while(!(event instanceof EndTransferEvent));
			 fail();
		}catch(IoxInvalidDataException ex){
			 assertError("no main objects of Test1.TopicB.TableB but linetable objects");
		}
	}
	@Test
	public void testAREAnoMainTable() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/AreaNoMainTable.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		try{
			 do{
			        event=reader.read();
			        if(event instanceof StartTransferEvent){
			        }else if(event instanceof StartBasketEvent){
			        }else if(event instanceof ObjectEvent){
			        	IomObject iomObj=((ObjectEvent)event).getIomObject();
			    		System.out.println(iomObj);
			    		assertNotNull(iomObj.getobjectoid());
			    		objs.put(iomObj.getobjectoid(), iomObj);
			        }else if(event instanceof EndBasketEvent){
			        }else if(event instanceof EndTransferEvent){
			        }
			 }while(!(event instanceof EndTransferEvent));
			 fail();
		}catch(IoxInvalidDataException ex){
			 assertError("no main objects of Test1.TopicB.TableB but linetable objects");
		}
	}
	@Test
	public void testSURFACEsubsequentTable() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceSubsequentTable.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		    		System.out.println(iomObj);
		    		assertNotNull(iomObj.getobjectoid());
		    		objs.put(iomObj.getobjectoid(), iomObj);
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		 assertEquals("Test1.TopicC.TableC0 oid 30 {}", 
				 objs.get("30").toString());
		 assertEquals("Test1.TopicC.TableC oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}}", 
				 objs.get("10").toString());
			
	}
	@Test
	public void testAREApriorTable() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/AreaPriorTable.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		    		System.out.println(iomObj);
		    		assertNotNull(iomObj.getobjectoid());
		    		objs.put(iomObj.getobjectoid(), iomObj);
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		 assertEquals("Test1.TopicD.TableD0 oid 30 {}", 
				 objs.get("30").toString());
		 assertEquals("Test1.TopicD.TableD oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}, _itf_Form COORD {C1 115.0, C2 115.0}}", 
				 objs.get("10").toString());
	}
	@Test
	public void testSURFACEthenAREA() throws Iox2jtsException, IoxException {
		ItfReader2 reader=new ItfReader2(new File("src/test/data/ItfReader2/SurfaceThenArea.itf"),false);
		reader.setModel(td);
		IoxEvent event=null;
		HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
		 do{
		        event=reader.read();
		        if(event instanceof StartTransferEvent){
		        }else if(event instanceof StartBasketEvent){
		        }else if(event instanceof ObjectEvent){
		        	IomObject iomObj=((ObjectEvent)event).getIomObject();
		    		System.out.println(iomObj);
		    		assertNotNull(iomObj.getobjectoid());
		    		assertNotNull(iomObj.getobjecttag());
		    		objs.put(iomObj.getobjecttag()+":"+iomObj.getobjectoid(), iomObj);
		        }else if(event instanceof EndBasketEvent){
		        }else if(event instanceof EndTransferEvent){
		        }
		 }while(!(event instanceof EndTransferEvent));
		 assertEquals("Test1.TopicE.TableE0 oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}}", 
				 objs.get("Test1.TopicE.TableE0:10").toString());
		 assertEquals("Test1.TopicE.TableE oid 10 {Form MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline POLYLINE {sequence SEGMENTS {segment [COORD {C1 110.0, C2 110.0}, COORD {C1 110.0, C2 140.0}, COORD {C1 120.0, C2 140.0}, COORD {C1 120.0, C2 110.0}, COORD {C1 110.0, C2 110.0}]}}}}}, _itf_Form COORD {C1 115.0, C2 115.0}}", 
				 objs.get("Test1.TopicE.TableE:10").toString());
	}
	
}
