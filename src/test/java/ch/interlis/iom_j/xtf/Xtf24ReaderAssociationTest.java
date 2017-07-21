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
	private static final String SPACE=" ";
	private static final String START_ELE_FAIL="unexpected start element"+SPACE;
	private static final String END_ELE_FAIL="unexpected end element"+SPACE;
	private static final String CHAR_ELE_FAIL="unexpected characters"+SPACE;
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

	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine eine Rolle in einer Embedded Association erstellt wird.
	@Test
	public void testEmbedded_1Assoc_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoEmbedded1to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Mensch.Mann oid oid1 {}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(0, attrCountA);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Mensch.Frau oid oid2 {bezMann -> Mensch.Mann REF {}}
		IomObject iomObjB=((ObjectEvent) event).getIomObject();
		String refOidB=iomObjB.getattrobj("bezMann", 0).getobjectrefoid();
		assertEquals("Mensch.Mann", refOidB);
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn 2 Association Klassen erstellt werden, welche beide auf die Selben Klassen eine Association haben.
	@Test
	public void testEmbedded_2Assoc_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoEmbedded1to1x2.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Atom.Proton oid oidP {electronD -> oidN REF {}}
		IomObject iomObjF=((ObjectEvent) event).getIomObject();
		String refOidF=iomObjF.getattrobj("electronD", 0).getobjectrefoid();
		assertEquals("oidN", refOidF);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Atom.Neutron oid oidN {electronA -> oidP REF {}}
		IomObject iomObjM=((ObjectEvent) event).getIomObject();
		String refOidM=iomObjM.getattrobj("electronA", 0).getobjectrefoid();
		assertEquals("oidP", refOidM);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn Stand Alone Rollen erstellt werden.
	@Test
	public void testStandalone_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAlone1ton.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule.Kinder oid oidK {}
		IomObject iomObjK=((ObjectEvent) event).getIomObject();
		int attrCountK=iomObjK.getattrcount();
		assertEquals(0, attrCountK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule.Lehrer oid oidL {}
		IomObject iomObjL=((ObjectEvent) event).getIomObject();
		int attrCountL=iomObjL.getattrcount();
		assertEquals(0, attrCountL);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule.Beziehung oid x {bezKinder -> oidK REF {}, bezLehrer -> oidL REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidK=iomObjA.getattrobj("bezLehrer", 0).getobjectrefoid();
		assertEquals("oidL", refOidK);
		String refOidL=iomObjA.getattrobj("bezKinder", 0).getobjectrefoid();
		assertEquals("oidK", refOidL);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn die Association ohne tid --> null erstellt wird.
	@Test
	public void testStandaloneWithoutTid_Ok() throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAloneNoTid.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule.Kinder oid oidK {}
		IomObject iomObjK=((ObjectEvent) event).getIomObject();
		int attrCountK=iomObjK.getattrcount();
		assertEquals(0, attrCountK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule.Lehrer oid oidL {}
		IomObject iomObjL=((ObjectEvent) event).getIomObject();
		int attrCountL=iomObjL.getattrcount();
		assertEquals(0, attrCountL);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule.Beziehung oid null {bezKinder -> oidK REF {}, bezLehrer -> oidL REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidK=iomObjA.getattrobj("bezLehrer", 0).getobjectrefoid();
		assertEquals("oidL", refOidK);
		String refOidL=iomObjA.getattrobj("bezKinder", 0).getobjectrefoid();
		assertEquals("oidK", refOidL);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn in einer Stand-Alone-Association:
	// Die Erste Rolle EXTENDED ist und 1 der 2 moeglichen Zielklassen die Kardinalitaet erfuellt.
	//Die Erste Rolle EXTENDED ist und 1 der 2 moeglichen Zielklassen die Kardinalitaet erfuellt und die Einschraenkung eingehalten wird.
	@Test
	public void testStandAlongeExtendedOrRestriction_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAlone1tonExtendedOr.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof ObjectEvent); // return Association.TopicG.ClassG1 oid oid1 {}
		IomObject iomObjG1=((ObjectEvent) event).getIomObject();
		int attrCountG1=iomObjG1.getattrcount();
		assertEquals(0, attrCountG1);
		
		event=reader.read();
		assertTrue(event instanceof ObjectEvent); // return Association.TopicG.ClassG2 oid oid2 {}
		IomObject iomObjG2=((ObjectEvent) event).getIomObject();
		int attrCountG2=iomObjG2.getattrcount();
		assertEquals(0, attrCountG2);
		
		event=reader.read();
		assertTrue(event instanceof ObjectEvent); // return Association.TopicG.ClassG3 oid oid3 {}
		IomObject iomObjG3=((ObjectEvent) event).getIomObject();
		int attrCountG3=iomObjG3.getattrcount();
		assertEquals(0, attrCountG3);
		
		event=reader.read();
		assertTrue(event instanceof ObjectEvent); // return Association.TopicG.ClassG4 oid oid4 {}
		IomObject iomObjG4=((ObjectEvent) event).getIomObject();
		int attrCountG4=iomObjG4.getattrcount();
		assertEquals(0, attrCountG4);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicG.assoG oid x {assoG1 [-> oid1 REF {}, -> oid2 REF {}], assoG2 [-> oid3 REF {}, -> oid4 REF {}]}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidG1=iomObjA.getattrobj("assoG1", 0).getobjectrefoid();
		assertEquals("oid1", refOidG1);
		String refOidG2=iomObjA.getattrobj("assoG2", 0).getobjectrefoid();
		assertEquals("oid3", refOidG2);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine eine Rolle in einer Stand-Alone-Association: External ist.
	@Test
	public void testStandAloneExternal_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAlone1tonExternal.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule2.Kinder oid oidK {}
		IomObject iomObjK=((ObjectEvent) event).getIomObject();
		int attrCountK=iomObjK.getattrcount();
		assertEquals(0, attrCountK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule2.Lehrer oid oidL {}
		IomObject iomObjL=((ObjectEvent) event).getIomObject();
		int attrCountL=iomObjL.getattrcount();
		assertEquals(0, attrCountL);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule2.Beziehung oid x {bezKinder -> oidK REF {}, bezLehrer -> oidL REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidK=iomObjA.getattrobj("bezLehrer", 0).getobjectrefoid();
		assertEquals("oidL", refOidK);
		String refOidL=iomObjA.getattrobj("bezKinder", 0).getobjectrefoid();
		assertEquals("oidK", refOidL);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine eine Rolle in einer Stand-Alone-Association: External und die andere Rolle: Ordered aufweist.
	@Test
	public void testStandAloneExternalOrdered_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAlone1tonExternalOrdered.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicD.ClassD1 oid oid1 {}
		IomObject iomObjD1=((ObjectEvent) event).getIomObject();
		int attrCountD1=iomObjD1.getattrcount();
		assertEquals(0, attrCountD1);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicD.ClassD2 oid oid2 {}
		IomObject iomObjD2=((ObjectEvent) event).getIomObject();
		int attrCountD2=iomObjD2.getattrcount();
		assertEquals(0, attrCountD2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicD.assoD12 oid x {assoD1 -> oid1 REF {}, assoD2 -> oid2 REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidD1=iomObjA.getattrobj("assoD1", 0).getobjectrefoid();
		assertEquals("oid1", refOidD1);
		String refOidD2=iomObjA.getattrobj("assoD2", 0).getobjectrefoid();
		assertEquals("oid2", refOidD2);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine eine Rolle in einer Stand-Alone-Association eine Kardinalitaet von 2-4 aufweist.
	@Test
	public void testStandAlone2to4_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAlone2to4.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicB.ClassC oid oid1 {}
		IomObject iomObjK=((ObjectEvent) event).getIomObject();
		int attrCountK=iomObjK.getattrcount();
		assertEquals(0, attrCountK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicB.ClassD oid oid2 {}
		IomObject iomObjL=((ObjectEvent) event).getIomObject();
		int attrCountL=iomObjL.getattrcount();
		assertEquals(0, attrCountL);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicB.ClassC oid oid3 {}
		IomObject iomObjK2=((ObjectEvent) event).getIomObject();
		int attrCountK2=iomObjK2.getattrcount();
		assertEquals(0, attrCountK2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicB.ClassD oid oid4 {}
		IomObject iomObjL2=((ObjectEvent) event).getIomObject();
		int attrCountL2=iomObjL2.getattrcount();
		assertEquals(0, attrCountL2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicB.assocCD oid x {assoC -> TopicB.ClassC REF {}, assoD -> TopicB.ClassD REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidK=iomObjA.getattrobj("assoD", 0).getobjectrefoid();
		assertEquals("TopicB.ClassD", refOidK);
		String refOidL=iomObjA.getattrobj("assoC", 0).getobjectrefoid();
		assertEquals("TopicB.ClassC", refOidL);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}

	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine eine Rolle in einer Embedded-Association eine Kardinalitaet von 0..1 aufweist.
	@Test
	public void testEmbedded0to1_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoEmbedded0to1.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicA.ClassA oid oid1 {}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCount=iomObjA.getattrcount();
		assertEquals(0, attrCount);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.TopicA.ClassB oid oid2 {roleA -> oid1 REF {}}
		IomObject iomObjB=((ObjectEvent) event).getIomObject();
		String refOid=iomObjB.getattrobj("roleA", 0).getobjectrefoid();
		assertEquals("oid1", refOid);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine eine Rolle in einer Stand-Alone-Association eine Kardinalitaet von {2} aufweist.
	@Test
	public void testStandAloneCard2_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAloneCard2.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule3.Kinder3 oid oid1 {}
		IomObject iomObjK=((ObjectEvent) event).getIomObject();
		int attrCountK=iomObjK.getattrcount();
		assertEquals(0, attrCountK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule3.Lehrer3 oid oid2 {}
		IomObject iomObjL=((ObjectEvent) event).getIomObject();
		int attrCountL=iomObjL.getattrcount();
		assertEquals(0, attrCountL);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule3.Kinder3 oid oid3 {}
		IomObject iomObjK2=((ObjectEvent) event).getIomObject();
		int attrCountK2=iomObjK2.getattrcount();
		assertEquals(0, attrCountK2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule3.Lehrer3 oid oid4 {}
		IomObject iomObjL2=((ObjectEvent) event).getIomObject();
		int attrCountL2=iomObjL2.getattrcount();
		assertEquals(0, attrCountL2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule3.Beziehung3 oid x {bezKinder3 -> Schule3.Kinder3 REF {}, bezLehrer3 -> Schule3.Lehrer3 REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidK=iomObjA.getattrobj("bezLehrer3", 0).getobjectrefoid();
		assertEquals("Schule3.Lehrer3", refOidK);
		String refOidL=iomObjA.getattrobj("bezKinder3", 0).getobjectrefoid();
		assertEquals("Schule3.Kinder3", refOidL);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
	
	// Es wird getestet, ob eine Fehlermeldung ausgegeben wird, wenn eine eine Rolle in einer Stand-Alone-Association eine Kardinalitaet von {*} aufweist.
	@Test
	public void testStandAloneCardN_Ok()  throws Iox2jtsException, IoxException {
		Xtf24Reader reader=new Xtf24Reader(new File(TEST_IN,"AssoAloneCardN.xml"));
		reader.setModel(td);
		assertTrue(reader.read() instanceof  StartTransferEvent);
		assertTrue(reader.read() instanceof  StartBasketEvent);
		
		IoxEvent event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule4.Kinder4 oid oid1 {}
		IomObject iomObjK=((ObjectEvent) event).getIomObject();
		int attrCountK=iomObjK.getattrcount();
		assertEquals(0, attrCountK);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule4.Lehrer4 oid oid2 {}
		IomObject iomObjL=((ObjectEvent) event).getIomObject();
		int attrCountL=iomObjL.getattrcount();
		assertEquals(0, attrCountL);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule4.Kinder4 oid oid3 {}
		IomObject iomObjK2=((ObjectEvent) event).getIomObject();
		int attrCountK2=iomObjK2.getattrcount();
		assertEquals(0, attrCountK2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule4.Lehrer4 oid oid4 {}
		IomObject iomObjL2=((ObjectEvent) event).getIomObject();
		int attrCountL2=iomObjL2.getattrcount();
		assertEquals(0, attrCountL2);
		
		event=reader.read();
		assertTrue(event instanceof  ObjectEvent); // return Association.Schule4.Beziehung4 oid x {bezLehrer4 -> Schule4.Lehrer4 REF {}, bezKinder4 -> Schule4.Kinder4 REF {}}
		IomObject iomObjA=((ObjectEvent) event).getIomObject();
		int attrCountA=iomObjA.getattrcount();
		assertEquals(2, attrCountA);
		String refOidK=iomObjA.getattrobj("bezLehrer4", 0).getobjectrefoid();
		assertEquals("Schule4.Lehrer4", refOidK);
		String refOidL=iomObjA.getattrobj("bezKinder4", 0).getobjectrefoid();
		assertEquals("Schule4.Kinder4", refOidL);
		
		assertTrue(reader.read() instanceof  EndBasketEvent);
		assertTrue(reader.read() instanceof  EndTransferEvent);
		reader.close();
		reader=null;
	}
}