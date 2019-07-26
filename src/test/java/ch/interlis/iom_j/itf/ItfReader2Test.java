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
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
		errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
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
