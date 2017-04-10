package ch.interlis.iox_j.filter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class TranslationFilterTest {

	private TransferDescription td=null;
	
	public void setUp(String iliFileName) throws Exception {
		// ili-datei lesen
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(iliFileName, FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	@Test
	public void simpleEnumToOrigin() throws Exception {
		setUp("src/test/data/filter/enumOk.ili");
		Iom_jObject iomObj=new Iom_jObject("EnumOkB.TopicB.ClassB", "o1");
		iomObj.setattrvalue("attrB", "b2.b21");
		Settings config=new Settings();
		TranslateToOrigin filter=new TranslateToOrigin(td, config);
		filter.filter(new StartTransferEvent());
		StartBasketEvent destStartBasketEvent=(StartBasketEvent) filter.filter(new StartBasketEvent("EnumOkB.TopicB","b1"));
		ObjectEvent destObjEvent=(ObjectEvent) filter.filter(new ObjectEvent(iomObj));
		filter.filter(new EndBasketEvent());
		filter.filter(new EndTransferEvent());
		
		assertEquals("EnumOkB.TopicB",destStartBasketEvent.getType());
		IomObject destObj = destObjEvent.getIomObject();
		assertEquals("EnumOkA.TopicA.ClassA",destObj.getobjecttag());
		assertEquals("a2.a21",destObj.getattrvalue("attrA"));
	}
	@Test
	public void simpleEnumToTranslation() throws Exception {
		setUp("src/test/data/filter/enumOk.ili");
		Iom_jObject iomObj=new Iom_jObject("EnumOkA.TopicA.ClassA", "o1");
		iomObj.setattrvalue("attrA", "a2.a21");
		Settings config=new Settings();
		TranslateToTranslation filter=new TranslateToTranslation(td, config);
		filter.filter(new StartTransferEvent());
		StartBasketEvent destStartBasketEvent=(StartBasketEvent) filter.filter(new StartBasketEvent("EnumOkB.TopicB","b1"));
		ObjectEvent destObjEvent=(ObjectEvent) filter.filter(new ObjectEvent(iomObj));
		filter.filter(new EndBasketEvent());
		filter.filter(new EndTransferEvent());
		
		assertEquals("EnumOkB.TopicB",destStartBasketEvent.getType());
		IomObject destObj = destObjEvent.getIomObject();
		assertEquals("EnumOkB.TopicB.ClassB",destObj.getobjecttag());
		assertEquals("b2.b21",destObj.getattrvalue("attrB"));
	}

}
