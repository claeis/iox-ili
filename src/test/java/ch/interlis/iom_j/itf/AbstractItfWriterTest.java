package ch.interlis.iom_j.itf;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxWriter;
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.jts.Iox2jtsException;

public abstract class AbstractItfWriterTest {
	protected final static String TEST_OUT="build";
    protected final static String TEST_IN="src/test/data/ItfWriter/";

	protected TransferDescription td=null;
	protected void addArc(IomObject polyline,double xa, double ya,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("ARC",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		ret.setattrvalue("A1", Double.toString(xa));
		ret.setattrvalue("A2", Double.toString(ya));
		sequence.addattrobj("segment",ret);
	}
	protected void addCoord(IomObject polyline,double x, double y){
		IomObject sequence=polyline.getattrobj("sequence",0);
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("COORD",null);
		ret.setattrvalue("C1", Double.toString(x));
		ret.setattrvalue("C2", Double.toString(y));
		sequence.addattrobj("segment",ret);
	}
	protected IomObject newPolyline()
	{
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("POLYLINE",null);
		IomObject sequence=new ch.interlis.iom_j.Iom_jObject("SEGMENTS",null);
		ret.addattrobj("sequence",sequence);
		return ret;
	}
	protected IomObject newPolygon() {
		// MULTISURFACE {surface SURFACE {boundary BOUNDARY {polyline
		IomObject ret=new ch.interlis.iom_j.Iom_jObject("MULTISURFACE",null);
		IomObject surface=new ch.interlis.iom_j.Iom_jObject("SURFACE",null);
		ret.addattrobj("surface",surface);
		return ret;
	}
	protected void addBoundary(IomObject polygon,IomObject polyline){
		IomObject boundary=new ch.interlis.iom_j.Iom_jObject("BOUNDARY",null);
		boundary.addattrobj("polyline", polyline);
		IomObject surface=polygon.getattrobj("surface",0);
		surface.addattrobj("boundary",boundary);
	}
    public static boolean fileEqual(File path1, File path2) throws IoxException {
        try {
            BufferedReader bf1 = null;
            BufferedReader bf2 = null;
            try{
                bf1 = new BufferedReader(new FileReader(path1));
                bf2 = new BufferedReader(new FileReader(path2));
                
                long lineNumber = 1;
                String line1 = "", line2 = "";
                while ((line1 = bf1.readLine()) != null) {
                    line2 = bf2.readLine();
                    if (line2 == null || !line1.equals(line2)) {
                        return false;
                    }
                    lineNumber++;
                }
                if (bf2.readLine() == null) {
                    return true;
                }else {
                    return false;
                }
            }finally {
                if(bf1!=null) {
                    bf1.close();
                    bf1=null;
                }
                if(bf2!=null) {
                    bf2.close();
                    bf2=null;
                }
            }
            
        }catch(IOException e) {
            throw new IoxException(e);
        }
    }   
    protected abstract IoxWriter createItfWriter(File out, TransferDescription td) throws IoxException;
    
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"Test1.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
    @Test
    public void testNoEvents() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestNoEvents.itf");
        File out=new File(TEST_OUT,"TestNoEvents-out.itf");
        IoxWriter writer=createItfWriter(out,td);
        writer.close();
        writer=null;
        assertTrue(fileEqual(in, out));
    }
	@Test
	public void testNoBaskets() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestNoBaskets.itf");
	    File out=new File(TEST_OUT,"TestNoBaskets-out.itf");
        IoxWriter writer=createItfWriter(out,td);
		writer.write(new StartTransferEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
        assertTrue(fileEqual(in, out));
	}
	@Test
	public void testEmptyBasket() throws Iox2jtsException, IoxException {
	    File in=new File(TEST_IN,"TestEmptyBasket.itf");
        File out=new File(TEST_OUT,"TestEmptyBasket-out.itf");
        IoxWriter writer=createItfWriter(out,td);
		writer.write(new StartTransferEvent());
		writer.write(new StartBasketEvent("Test1.TopicF","bid1"));
		writer.write(new EndBasketEvent());
		writer.write(new EndTransferEvent());
		writer.close();
		writer=null;
		assertTrue(fileEqual(in, out));
	}
    @Test
    public void testEmptyBasketMandatoryTable() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestEmptyBasketMandatoryTable.itf");
        File out=new File(TEST_OUT,"TestEmptyBasketMandatoryTable-out.itf");
        IoxWriter writer=createItfWriter(out,td);
        writer.write(new StartTransferEvent());
        writer.write(new StartBasketEvent("Test1.TopicG","bid1"));
        writer.write(new EndBasketEvent());
        writer.write(new EndTransferEvent());
        writer.close();
        writer=null;
        assertTrue(fileEqual(in, out));
    }

	
}
