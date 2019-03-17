package ch.interlis.iom_j.iom2java;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.LogCollector;
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox_j.validator.Validator;

public class Iom2JavaToolTest {
	
	private final static String TEST_IN="src/test/data/Iom2Java/";
	private TransferDescription td=null;
	
	@Before
	public void setup() throws Ili2cFailure
	{
		// compile model
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_IN+"model.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);
	}
	
	// Es wird getestet ob eine Surface und eine Area innerhalb der xtf Datei in java vollstaendig dargestetllt wird
	// und in Anschluss kompilieren kann.
	@Test
	public void multiSurface() throws IoxException{
		Iom2JavaTool iom2java=new Iom2JavaTool();
		iom2java.xtf2java(new File(TEST_IN,"MultiSurface.xtf"));
	}
	
	// Es wird getestet ob die Referenz-Attribute-Werte innerhalb der xtf Datei in java vollstaendig dargestetllt werden
	// und in Anschluss kompilieren koennen.
	@Test
	public void references_Ok() throws IoxException{
		Iom2JavaTool iom2java=new Iom2JavaTool();
		iom2java.xtf2java(new File(TEST_IN, "References.xtf"));
	}
	
	@Test
	public void compileOutputTest_MultiSurface_Ok() throws IoxException, Ili2cFailure {
		try {
			//////////////////// copy/paste output here //////////////////////
			ArrayList<IoxEvent> events=new ArrayList<IoxEvent>();
		    StartTransferEvent startTransferEvent=new StartTransferEvent();
		    events.add(startTransferEvent);
		    {
		        // basket_ab.cd
		        StartBasketEvent startBasketEvent_ab_cd=new StartBasketEvent("Model.Topic","ab.cd");
		        events.add(startBasketEvent_ab_cd);
		        EndBasketEvent endBasketEvent_ab_cd=new EndBasketEvent();
		        events.add(endBasketEvent_ab_cd);
		    }
		    {
		        // basket_ef.gh
		        StartBasketEvent startBasketEvent_ef_gh=new StartBasketEvent("Model.Topic","ef.gh");
		        events.add(startBasketEvent_ef_gh);
		        {
		            // object_oid.1
		            Iom_jObject object_oid_1=new Iom_jObject("Model.Topic.Surface","oid.1");
		            object_oid_1.setattrvalue("attr1", "xy");
		            IomObject object_attr2=object_oid_1.addattrobj("attr2", "MULTISURFACE");
		            IomObject object_surface=object_attr2.addattrobj("surface", "SURFACE");
		            IomObject object_boundary=object_surface.addattrobj("boundary", "BOUNDARY");
		            IomObject object_polyline=object_boundary.addattrobj("polyline", "POLYLINE");
		            IomObject object_sequence=object_polyline.addattrobj("sequence", "SEGMENTS");
		            IomObject object_segment=object_sequence.addattrobj("segment", "COORD");
		            object_segment.setattrvalue("C1", "2600000.000");
		            object_segment.setattrvalue("C2", "1050000.000");
		            IomObject object_segment2=object_sequence.addattrobj("segment", "COORD");
		            object_segment2.setattrvalue("C1", "2600000.000");
		            object_segment2.setattrvalue("C2", "1050025.000");
		            IomObject object_segment3=object_sequence.addattrobj("segment", "ARC");
		            object_segment3.setattrvalue("A1", "2600000.030");
		            object_segment3.setattrvalue("A2", "1050024.367861");
		            object_segment3.setattrvalue("C1", "2600024.98");
		            object_segment3.setattrvalue("C2", "1050014.367861");
		            IomObject object_segment4=object_sequence.addattrobj("segment", "COORD");
		            object_segment4.setattrvalue("C1", "2600024.98");
		            object_segment4.setattrvalue("C2", "1050000.000");
		            IomObject object_segment5=object_sequence.addattrobj("segment", "COORD");
		            object_segment5.setattrvalue("C1", "2600000.000");
		            object_segment5.setattrvalue("C2", "1050000.000");
		            ObjectEvent objEvent_oid_1=new ObjectEvent(object_oid_1);
		            events.add(objEvent_oid_1);
		        }
		        {
		            // object_oid.2
		            Iom_jObject object_oid_2=new Iom_jObject("Model.Topic.Area","oid.2");
		            object_oid_2.setattrvalue("attr1", "wk");
		            IomObject object_attr3=object_oid_2.addattrobj("attr3", "MULTISURFACE");
		            IomObject object_surface=object_attr3.addattrobj("surface", "SURFACE");
		            IomObject object_boundary=object_surface.addattrobj("boundary", "BOUNDARY");
		            IomObject object_polyline=object_boundary.addattrobj("polyline", "POLYLINE");
		            IomObject object_sequence=object_polyline.addattrobj("sequence", "SEGMENTS");
		            IomObject object_segment=object_sequence.addattrobj("segment", "COORD");
		            object_segment.setattrvalue("C1", "2600000.000");
		            object_segment.setattrvalue("C2", "1050000.000");
		            IomObject object_segment2=object_sequence.addattrobj("segment", "COORD");
		            object_segment2.setattrvalue("C1", "2600000.000");
		            object_segment2.setattrvalue("C2", "1050025.000");
		            IomObject object_segment3=object_sequence.addattrobj("segment", "ARC");
		            object_segment3.setattrvalue("A1", "2600000.030");
		            object_segment3.setattrvalue("A2", "1050024.367861");
		            object_segment3.setattrvalue("C1", "2600024.98");
		            object_segment3.setattrvalue("C2", "1050014.367861");
		            IomObject object_segment4=object_sequence.addattrobj("segment", "COORD");
		            object_segment4.setattrvalue("C1", "2600024.98");
		            object_segment4.setattrvalue("C2", "1050000.000");
		            IomObject object_segment5=object_sequence.addattrobj("segment", "COORD");
		            object_segment5.setattrvalue("C1", "2600000.000");
		            object_segment5.setattrvalue("C2", "1050000.000");
		            ObjectEvent objEvent_oid_2=new ObjectEvent(object_oid_2);
		            events.add(objEvent_oid_2);
		        }
		        EndBasketEvent endBasketEvent_ef_gh=new EndBasketEvent();
		        events.add(endBasketEvent_ef_gh);
		    }
		    EndTransferEvent endTransferEvent=new EndTransferEvent();
		    events.add(endTransferEvent);
		    //////////////////////////////////////////////////////////////////
		    ValidationConfig modelConfig=new ValidationConfig();
			LogCollector logger=new LogCollector();
			LogEventFactory errFactory=new LogEventFactory();
			Settings settings=new Settings();
			Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
			for(int i=0;i<events.size();i++) {
				validator.validate(events.get(i));
			}
			// Asserts
			assertTrue(logger.getErrs().size()==0);
		}catch(Exception e) {
			throw new IoxException(e);
		}
	}
	
	@Test
	public void compileOutputTest_References() throws IoxException, Ili2cFailure {
		try {
			//////////////////// copy/paste output here //////////////////////
			ArrayList<IoxEvent> events=new ArrayList<IoxEvent>();
		    StartTransferEvent startTransferEvent=new StartTransferEvent();
		    events.add(startTransferEvent);
		    {
		        // basket_b1
		        StartBasketEvent startBasketEvent_b1=new StartBasketEvent("Model.Topic","b1");
		        events.add(startBasketEvent_b1);
		        {
		            // object_o1
		            Iom_jObject object_o1=new Iom_jObject("Model.Topic.ClassA","o1");
		            object_o1.setattrvalue("attr1", "abc");
		            ObjectEvent objEvent_o1=new ObjectEvent(object_o1);
		            events.add(objEvent_o1);
		        }
		        {
		            // object_o2
		            Iom_jObject object_o2=new Iom_jObject("Model.Topic.ClassRef","o2");
		            IomObject object_attr1=object_o2.addattrobj("attr1", "Model.Topic.CodeReference");
		            IomObject object_ref=object_attr1.addattrobj("ref", "REF");
		            ObjectEvent objEvent_o2=new ObjectEvent(object_o2);
		            events.add(objEvent_o2);
		        }
		        EndBasketEvent endBasketEvent_b1=new EndBasketEvent();
		        events.add(endBasketEvent_b1);
		    }
		    EndTransferEvent endTransferEvent=new EndTransferEvent();
		    events.add(endTransferEvent);
		    //////////////////////////////////////////////////////////////////
		    ValidationConfig modelConfig=new ValidationConfig();
			LogCollector logger=new LogCollector();
			LogEventFactory errFactory=new LogEventFactory();
			Settings settings=new Settings();
			Validator validator=new Validator(td, modelConfig,logger,errFactory,settings);
			for(int i=0;i<events.size();i++) {
				validator.validate(events.get(i));
			}
			// Asserts
			assertTrue(logger.getErrs().size()==0);
		}catch(Exception e) {
			throw new IoxException(e);
		}
	}
}