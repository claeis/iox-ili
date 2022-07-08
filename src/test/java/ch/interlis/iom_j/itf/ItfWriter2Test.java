package ch.interlis.iom_j.itf;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.ItfWriter2;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxWriter;
import ch.interlis.iox_j.*;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class ItfWriter2Test extends AbstractItfWriterTest {
    @Override
    protected IoxWriter createItfWriter(File out, TransferDescription td) throws IoxException {
        return new ItfWriter2(out,td);
    }
    @Test
    public void testSimple() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestSimple.itf");
        File out=new File(TEST_OUT,"TestSimple-out.itf");
        IoxWriter writer=createItfWriter(out,td);
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
        assertTrue(itfFileEqual(in, out));
    }
    @Test
    public void testSurfaceNull() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestSurfaceNull.itf");
        File out=new File(TEST_OUT,"TestSurfaceNull-out.itf");
        IoxWriter writer=createItfWriter(out,td);
        writer.write(new StartTransferEvent());
        writer.write(new StartBasketEvent("Test1.TopicA","bid1"));
        writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicA.TableA","10")));
        writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicA.TableA","11")));
        writer.write(new EndBasketEvent());
        writer.write(new EndTransferEvent());
        writer.close();
        writer=null;
        assertTrue(itfFileEqual(in, out));
    }
    @Test
    public void testSurface() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestSurfaceWithArc.itf");
        File out=new File(TEST_OUT,"TestSurfaceWithArc-out.itf");
        IoxWriter writer=createItfWriter(out,td);
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
        assertTrue(itfFileEqual(in, out));
    }
    @Test
    public void testAreaEmpty() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestAreaEmpty.itf");
        File out=new File(TEST_OUT,"TestAreaEmpty-out.itf");
        IoxWriter writer=createItfWriter(out,td);
        writer.write(new StartTransferEvent());
        writer.write(new StartBasketEvent("Test1.TopicB","bid1"));
        writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicB.TableB","10")));
        writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicB.TableB","11")));
        writer.write(new EndBasketEvent());
        writer.write(new EndTransferEvent());
        writer.close();
        writer=null;
        assertTrue(itfFileEqual(in, out));
    }
    @Test
    public void testAreaSimple() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestAreaSimple.itf");
        File out=new File(TEST_OUT,"TestAreaSimple-out.itf");
        IoxWriter writer=createItfWriter(out,td);
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
        assertTrue(itfFileEqual(in, out));
    }
    @Test
    public void testAreaWithArc() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestAreaWithArc.itf");
        File out=new File(TEST_OUT,"TestAreaWithArc-out.itf");
        IoxWriter writer=createItfWriter(out,td);
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
        assertTrue(itfFileEqual(in, out));
    }
    @Test
    public void testAreaWithHole() throws Iox2jtsException, IoxException {
        File in=new File(TEST_IN,"TestAreaWithHole.itf");
        File out=new File(TEST_OUT,"TestAreaWithHole-out.itf");
        IoxWriter writer=createItfWriter(out,td);
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
        assertTrue(itfFileEqual(in, out));
    }
	
}
