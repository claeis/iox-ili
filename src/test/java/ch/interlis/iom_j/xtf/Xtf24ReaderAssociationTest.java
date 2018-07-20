package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
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
	public void embedded_0to1_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"Embedded_0to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Mann oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Mann oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Frau oid oid2 {}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Auto oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Garage oid oid2 {bezAuto -> oid1 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Schule.Kinder oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Schule.Lehrer oid oid2 {bezKinder -> oid1 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Firma.Admin oid oid1 {bezUser -> oid2 REF {}}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Firma.User oid oid2 {}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Atom.Proton oid oidP {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Atom.Neutron oid oidN {electronA -> oidP REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Transport.Schiff oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Transport.Hafen oid oid2 {bezSchiff -> oid1, ORDER_POS 6 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum.Ast oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum.Blatt oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum.Beziehung {bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum.Ast oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum.Blatt oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum2.Beziehung {attr1 5, attr2 6, attr3 7, bezAst -> oid1 REF {}, bezBlatt -> oid2 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassB oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.Beziehung {roleA -> oid1 REF {}, roleB -> oid2 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail3.Ast oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail3.Blatt oid oid2 {}
		IoxEvent event=reader.read();
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassB oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.Beziehung {roleA -> oid1 REF {}, roleB -> oid1 REF {}}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.ClassB oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail1.Beziehung {roleA [-> oid1 REF {}, -> oid2 REF {}], roleB [-> oid1 REF {}, -> oid2 REF {}]}
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
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail2.ClassA oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail2.ClassA oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Fail2.Beziehung {roleA -> oid1 REF {}, roleB -> oid2 REF {}}
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
			assertTrue((ioxEx).getMessage().contains("ili:delete references are not yet implemented."));
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
			assertTrue((ioxEx).getMessage().contains("unexpected element: roleC"));
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
	
	// Die Beziehungsklasse hat nur eine Rolle.
	@Test
	public void associationWithOneRole_Fail() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssociationWithOneRole.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum.Ast oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Baum.Blatt oid oid2 {}
		try{
			reader.read(); // Association.Baum.Beziehung {bezAst -> oid1 REF {}}
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("expected at least 2 roles in ASSOCIATION Association.Baum.Beziehung"));
	        assertTrue(ioxEx instanceof IoxException);
		}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
}