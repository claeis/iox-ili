package ch.interlis.iom_j.xtf;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.jts.Iox2jtsException;

public class Xtf23ReaderAssociationTest {

	private final static String TEST_IN="src/test/data/Xtf23Reader/associations";
	
	// prueft, ob eine eingebettete Referenz erstellt werden kann.
	@Test
	public void embedded_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Embedded_0to1.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Mann oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Frau oid oid2 {bezMann -> oid1 REF {}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob eine stand-alone Beziehung mit Attributen erstellt werden kann.
	@Test
	public void standAlone_WithAttributes_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"StandAlone_WithAttributes.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Mann oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Frau oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Beziehung {attr1 text1, attr2 text2, attr3 text3, bezFrau -> oid2 REF {}, bezMann -> oid1 REF {}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob eine SETORDERPOS Klasse erstellt werden kann.
	@Test
	public void setOrderPos_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"SetOrderPos.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Mann1 oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Frau2 oid oid2 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Mann3 oid oid3 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.Frau4 oid oid4 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Mensch.SETORDERPOS oid oid5 {role1 -> oid1, ORDER_POS 1 REF {}, role2 -> oid2, ORDER_POS 2 REF {}, role3 -> oid3, ORDER_POS 3 REF {}, role4 -> oid4, ORDER_POS 4 REF {}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob innerhalb einer eingebetteten Referenz eine Reihenfolge-Positionierung erstellt werden kann.
	@Test
	public void embedded_1to1_OrderPos_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Embedded_1to1_OrderPos.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Transport.Schiff oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Transport.Hafen oid oid2 {bezSchiff -> oid1, ORDER_POS 6 REF {}}
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// prueft, ob eine Stand-Alone Beziehung erstellt werden kann.
	@Test
	public void standAlone_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"StandAlone.xtf"));
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
	
	// prueft, ob Kommentare innerhalb von Associations richtig erkannt werden.
	@Test
	public void commentsInsideAssociation_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"CommentsInsideAssociation.xtf"));
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
	
	// prueft, ob beide Rollen auf die Klasse: ClassA mit der oid1 referenzieren.
	// Die Rolle roleB sollte auf die Klasse: ClassB mit der oid2 schauen.
	// Dieser Fehlerfall soll 1 zu 1 uebergeben werden und erst im ili-validator als Fehler ausgegeben werden.
	@Test
	public void sameTargetClass_Ok() throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"SameTargetClass.xtf"));
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
		
	// prueft, ob eine Fehlermeldung ausgegeben wird, wenn ein Delete Object erstellt wird.
	@Test
	public void deleteObjectWithRef_Fail()  throws Iox2jtsException, IoxException {
		Xtf23Reader reader=new Xtf23Reader(new File(TEST_IN,"Embedded_1to1_DeleteRef.xtf"));
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Auto oid oid1 {}
		assertTrue(reader.read() instanceof  ObjectEvent); // Association.Autos.Garage oid oid2 {bezAuto -> oid1 REF {}}
		try{
			reader.read();
			fail();
		}catch(IoxException ioxEx){
			assertTrue((ioxEx).getMessage().contains("delete references are not yet implemented."));
	        assertTrue(ioxEx instanceof IoxException);
		}
		reader.close();
		reader=null;
	}
}