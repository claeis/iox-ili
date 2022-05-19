package ch.interlis.iom_j.itf;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxWriter;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class ItfWriterTest extends AbstractItfWriterTest {
    @Override
    protected IoxWriter createItfWriter(File out, TransferDescription td) throws IoxException {
        return new ItfWriter(out,td);
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
        try {
            writer.write(new ObjectEvent(new Iom_jObject("Test1.TopicF.TableF0","11"))); // wrong order of objects
            fail();
        }catch(IoxException ex) {
            
        }
    }
}
