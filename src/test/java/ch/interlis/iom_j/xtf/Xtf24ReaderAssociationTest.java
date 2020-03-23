package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf24ReaderAssociationTest {

	private final static String TEST_IN="src/test/data/Xtf24Reader/associations";
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"/Association.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
    @Test
    public void embeddedAssociationWithAttributes_Ok() throws Iox2jtsException, IoxException {
        Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"EmbeddedAssociationWithAttributes.xml"));
        reader.setModel(td);
        assertTrue(reader.read() instanceof  StartTransferEvent);
        assertTrue(reader.read() instanceof  StartBasketEvent);
        
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("Association.TopicEmbeddedAssWithAttr.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        assertEquals("Association.TopicEmbeddedAssWithAttr.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());     
        
        IomObject association = iomObject.getattrobj("rolle_A", 0);
        assertNotNull(association);
        assertEquals("oid1", association.getobjectrefoid());
        assertEquals("12", association.getattrvalue("attr_Assoc"));
        
        assertTrue(reader.read() instanceof  EndBasketEvent);
        assertTrue(reader.read() instanceof  EndTransferEvent);
        reader.close();
        reader=null;
    }

	@Test
	public void embedded_0to1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_0to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Mann oid oid1 {}
        assertEquals("Association.Mensch.Mann", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}}
        assertEquals("Association.Mensch.Frau", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        IomObject bezMann = iomObject.getattrobj("bezMann", 0);
        assertNotNull(bezMann);
        assertEquals("oid1", bezMann.getobjectrefoid());
        assertEquals("REF", bezMann.getobjecttag());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es soll keine Referenz erstellt werden.
	@Test
	public void embedded_0to0_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_0_0.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Mann oid oid1 {}
        assertEquals("Association.Mensch.Mann", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau oid oid2 {}
        assertEquals("Association.Mensch.Frau", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}

	@Test
	public void embedded_1to1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Auto oid oid1 {}
        assertEquals("Association.Autos.Auto", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());

        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}}
        assertEquals("Association.Autos.Garage", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        IomObject bezAuto = iomObject.getattrobj("bezAuto", 0);
        assertNotNull(bezAuto);
        assertEquals("oid1", bezAuto.getobjectrefoid());
        assertEquals("REF", bezAuto.getobjecttag());
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void embedded_1toN_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1toN.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Schule.Kinder oid oid1 {}
        assertEquals("Association.Schule.Kinder", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());


        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}}
        assertEquals("Association.Schule.Lehrer", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        IomObject bezKinder = iomObject.getattrobj("bezKinder", 0);
        assertNotNull(bezKinder);
        assertEquals("oid1", bezKinder.getobjectrefoid());
        assertEquals("REF", bezKinder.getobjecttag());
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void embedded_Nto1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_Nto1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Firma.Admin oid oid1 {bezUser -> oid2 REF {}}
        assertEquals("Association.Firma.Admin", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());

        IomObject bezUser = iomObject.getattrobj("bezUser", 0);
        assertNotNull(bezUser);
        assertEquals("oid2", bezUser.getobjectrefoid());
        assertEquals("REF", bezUser.getobjecttag());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Firma.User oid oid2 {}
        assertEquals("Association.Firma.User", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void embedded2_1to1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded2_1to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Atom.Proton oid oidP {}
        assertEquals("Association.Atom.Proton", iomObject.getobjecttag());
        assertEquals("oidP", iomObject.getobjectoid());

        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Atom.Neutron oid oidN {electronA -> oidP REF {}}
        assertEquals("Association.Atom.Neutron", iomObject.getobjecttag());
        assertEquals("oidN", iomObject.getobjectoid());
        
        IomObject electronA = iomObject.getattrobj("electronA", 0);
        assertNotNull(electronA);
        assertEquals("oidP", electronA.getobjectrefoid());
        assertEquals("REF", electronA.getobjecttag());
         
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void embedded_1to1_OrderPos_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1to1_OrderPos.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Transport.Schiff oid oid1 {}
        assertEquals("Association.Transport.Schiff", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Transport.Hafen oid oid2 {bezSchiff -> oid1, ORDER_POS 6 REF {}}
        assertEquals("Association.Transport.Hafen", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        IomObject bezSchiff = iomObject.getattrobj("bezSchiff", 0);
        assertNotNull(bezSchiff);
        assertEquals("oid1", bezSchiff.getobjectrefoid());
        assertEquals("REF", bezSchiff.getobjecttag());
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void alone_NtoN_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Alone_NtoN.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Ast oid oid1 {}
        assertEquals("Association.Baum.Ast", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Blatt oid oid2 {}
        assertEquals("Association.Baum.Blatt", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Beziehung {bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}}
        assertEquals("Association.Baum.Beziehung", iomObject.getobjecttag());
        
        // bezAst
        IomObject bezAst = iomObject.getattrobj("bezAst", 0);
        assertNotNull(bezAst);
        assertEquals("oid1", bezAst.getobjectrefoid());
        assertEquals("REF", bezAst.getobjecttag());
        
        //
        IomObject bezBlatt = iomObject.getattrobj("bezBlatt", 0);
        assertNotNull(bezBlatt);
        assertEquals("oid2", bezBlatt.getobjectrefoid());
        assertEquals("REF", bezBlatt.getobjecttag());

		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void xml1Line_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Xml1Line.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Ast oid oid1 {}
        assertEquals("Association.Baum.Ast", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Blatt oid oid2 {}
        assertEquals("Association.Baum.Blatt", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Beziehung {bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}}
        assertEquals("Association.Baum.Beziehung", iomObject.getobjecttag());
        
        // bezAst
        IomObject bezAst = iomObject.getattrobj("bezAst", 0);
        assertNotNull(bezAst);
        assertEquals("oid1", bezAst.getobjectrefoid());
        assertEquals("REF", bezAst.getobjecttag());
        
        // bezBlatt
        IomObject bezBlatt = iomObject.getattrobj("bezBlatt", 0);
        assertNotNull(bezBlatt);
        assertEquals("oid2", bezBlatt.getobjectrefoid());
        assertEquals("REF", bezBlatt.getobjecttag());
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	@Test
	public void alone_WithAttributes_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Alone_WithAttributes.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		

        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Ast oid oid1 {}
        assertEquals("Association.Baum2.Ast", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum.Blatt oid oid2 {}
        assertEquals("Association.Baum2.Blatt", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Baum2.Beziehung {attr1 5, attr2 6, attr3 7, bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}}
        assertEquals("Association.Baum2.Beziehung", iomObject.getobjecttag());
        assertEquals("5", iomObject.getattrvalue("attr1"));
        assertEquals("6", iomObject.getattrvalue("attr2"));
        assertEquals("7", iomObject.getattrvalue("attr3"));

        // bezAst
        IomObject bezAst = iomObject.getattrobj("bezAst", 0);
        assertNotNull(bezAst);
        assertEquals("oid1", bezAst.getobjectrefoid());
        assertEquals("REF", bezAst.getobjecttag());
        
        // bezBlatt
        IomObject bezBlatt = iomObject.getattrobj("bezBlatt", 0);
        assertNotNull(bezBlatt);
        assertEquals("oid2", bezBlatt.getobjectrefoid());
        assertEquals("REF", bezBlatt.getobjecttag());        

		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Kommentare innerhalb von Associations muessen ignoriert werden.
	@Test
	public void commentsInsideAssociation_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"CommentsInsideAssociation.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassA oid oid1 {}
        assertEquals("Association.Fail1.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassB oid oid2 {}
        assertEquals("Association.Fail1.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
		
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("Association.Fail1.Beziehung", iomObject.getobjecttag());
        
        // roleA
        IomObject bezAst = iomObject.getattrobj("roleA", 0);
        assertNotNull(bezAst);
        assertEquals("oid1", bezAst.getobjectrefoid());
        assertEquals("REF", bezAst.getobjecttag());
        
        // roleB
        IomObject bezBlatt = iomObject.getattrobj("roleB", 0);
        assertNotNull(bezBlatt);
        assertEquals("oid2", bezBlatt.getobjectrefoid());
        assertEquals("REF", bezBlatt.getobjecttag()); 
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Keinen Namen fuer die Beziehung erstellt, deshalb wurde als Name: role1role2 genommen.
	@Test
	public void noAssociationName_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"NoAssociationName.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail3.Ast oid oid1 {}
        assertEquals("Association.Fail3.Ast", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail3.ClassB oid oid2 {}
        assertEquals("Association.Fail3.Blatt", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // Association.Fail3.bezAstbezBlatt {bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}}
		IomObject iomObj1=((ObjectEvent) event).getIomObject();
		String className=iomObj1.getobjecttag();
		assertEquals("Association.Fail3.bezAstbezBlatt", className); // bezAst(role1) + bezBlatt(role2)
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Beide Rollen schauen auf die Klasse: ClassA mit der oid1.
	// Die Rolle roleB sollte auf die Klasse: ClassB mit der oid2 schauen.
	// Dieser Fehlerfall soll 1 zu 1 uebergeben werden und erst im ili-validator als Fehler ausgegeben werden.
	@Test
	public void sameTargetClass_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"SameTargetClass.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassA oid oid1 {}
        assertEquals("Association.Fail1.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassB oid oid2 {}
        assertEquals("Association.Fail1.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
		
        // Association.Fail1.Beziehung {roleA -> oid1 REF {}, roleB -> oid1 REF {}}        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("Association.Fail1.Beziehung", iomObject.getobjecttag());
        
        // roleA
        IomObject roleA = iomObject.getattrobj("roleA", 0);
        assertNotNull(roleA);
        assertEquals("oid1", roleA.getobjectrefoid());
        assertEquals("REF", roleA.getobjecttag());
        
        // roleB
        IomObject roleB = iomObject.getattrobj("roleB", 0);
        assertNotNull(roleB);
        assertEquals("oid1", roleB.getobjectrefoid());
        assertEquals("REF", roleB.getobjecttag()); 
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Die Beziehung hat mehr Rollen als im ili Model definiert wurden.
	// Da die Basis Klasse und die Ziel Klasse uebereinstimmen, soll keine Fehlermeldung ausgegeben werden.
	@Test
	public void moreRolesThanDefined_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"MoreRolesThanDefined.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassA oid oid1 {}
        assertEquals("Association.Fail1.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.ClassB oid oid2 {}
        assertEquals("Association.Fail1.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail1.Beziehung {roleA [-> oid1 REF {}, -> oid2 REF {}], roleB [-> oid1 REF {}, -> oid2 REF {}]}
        assertEquals("Association.Fail1.Beziehung", iomObject.getobjecttag());
        
        // Role 1
        IomObject roleA = iomObject.getattrobj("roleA", 0);
        assertNotNull(roleA);
        assertEquals("oid1", roleA.getobjectrefoid());
        assertEquals("REF", roleA.getobjecttag());
        
        IomObject roleA1 = iomObject.getattrobj("roleA", 1);
        assertNotNull(roleA1);
        assertEquals("oid2", roleA1.getobjectrefoid());
        assertEquals("REF", roleA1.getobjecttag());
        
        // Role 2
        IomObject roleB = iomObject.getattrobj("roleB", 0);
        assertNotNull(roleB);
        assertEquals("oid1", roleB.getobjectrefoid());
        assertEquals("REF", roleB.getobjecttag());
        
        IomObject roleB2 = iomObject.getattrobj("roleB", 1);
        assertNotNull(roleB2);
        assertEquals("oid2", roleB2.getobjectrefoid());
        assertEquals("REF", roleB2.getobjecttag());
        
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Die Rolle 1 hat eine 0 zu 0 Beziehung.
	// Falls unter keinen Umstaenden zwischen 2 ili-Objekten eine Beziehung existieren darf, ist diese Funktion nuetzlich.
	@Test
	public void valid_0to0Association_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Valid0to0Association.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);

        IoxEvent event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        IomObject iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail2.ClassA oid oid1 {}
        assertEquals("Association.Fail2.ClassA", iomObject.getobjecttag());
        assertEquals("oid1", iomObject.getobjectoid());
        
        
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        
        // Association.Fail2.ClassB oid oid2 {}
        assertEquals("Association.Fail2.ClassB", iomObject.getobjecttag());
        assertEquals("oid2", iomObject.getobjectoid());
        
        // Association.Fail2.Beziehung {roleA -> oid1 REF {}, roleB -> oid2 REF {}}
        event = reader.read();
        assertTrue(event instanceof ObjectEvent);
        iomObject = ((ObjectEvent) event).getIomObject();
        assertEquals("Association.Fail2.Beziehung", iomObject.getobjecttag());
        
        // roleA
        IomObject roleA = iomObject.getattrobj("roleA", 0);
        assertNotNull(roleA);
        assertEquals("oid1", roleA.getobjectrefoid());
        assertEquals("REF", roleA.getobjecttag());
        
        // roleB
        IomObject roleB = iomObject.getattrobj("roleB", 0);
        assertNotNull(roleB);
        assertEquals("oid2", roleB.getobjectrefoid());
        assertEquals("REF", roleB.getobjecttag());
		 
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet ob ein Delete Object mit einer tid und einer Reference erstellt werden kann.
	@Test
	public void testDeleteObjectWithRef_Fail()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_1to1_DeleteRef.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Auto oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Garage oid oid2 {bezAuto -> oid1 REF {}}
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("ili:delete links without tid is not yet implemented."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Die Rolle welche innerhalb der Association erstellt wurde, existiert im Model nicht.
	@Test
	public void roleNotExist_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"RoleNotExist.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassA oid oid2 {}
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertEquals("line 17: unexpected element: {http://www.interlis.ch/xtf/2.4/Association}roleC in Association.Fail1.Beziehung",(ioxEx).getMessage());
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
	
	// Die Association existiert nicht innerhalb des ili Models.
	@Test
	public void associationNotExist_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssociationNotExist.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassA oid oid2 {}
		try{
			reader.read(); // Beziehung2
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("class or association Beziehung2 not found"));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
}