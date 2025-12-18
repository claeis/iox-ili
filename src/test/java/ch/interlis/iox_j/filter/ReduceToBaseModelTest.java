package ch.interlis.iox_j.filter;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.Model;
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
import ch.interlis.iox_j.validator.ValidatorTestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReduceToBaseModelTest {
    private static final String ILI_FILE = "src/test/data/filter/ReduceToBaseModelTest.ili";
    private static final String CATALOGUE_MODEL = "TestCatalogue";
    private static final String CATALOGUE_TOPIC = CATALOGUE_MODEL + ".Catalogues";
    private static final String CATALOGUE_CLASS = CATALOGUE_TOPIC + ".CatalogueClass";
    private static final String CATALOGUE_CLASS_REF = CATALOGUE_TOPIC + ".CatalogueClassRef";
    private static final String BASE_MODEL = "BaseModel";
    private static final String BASE_TOPIC = BASE_MODEL + ".TopicA";
    private static final String BASE_CLASS = BASE_TOPIC + ".ClassA";
    private static final String BASE_CLASS_B = BASE_TOPIC + ".ClassB";
    private static final String EXTENDED_MODEL = "ExtendedModel";
    private static final String EXTENDED_TOPIC = EXTENDED_MODEL + ".TopicA";
    private static final String EXTENDED_CLASS = EXTENDED_TOPIC + ".ClassA";
    private static final String EXTENDED_CLASS_B = EXTENDED_TOPIC + ".ClassB";
    private static final String EXTENDED_CLASS_C = EXTENDED_TOPIC + ".ClassC";

    private TransferDescription td;
    private ReduceToBaseModel reduceToBaseModel;
    private Settings settings;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
        settings = new Settings();
    }

    @Test
    public void reduceToBaseModelReducesEnumValue() throws IoxException {
        List<Model> models = Collections.singletonList((Model) td.getElement(BASE_MODEL));
        reduceToBaseModel = new ReduceToBaseModel(models, td, settings);

        assertUnfiltered(new StartTransferEvent());

        assertUnfiltered(new StartBasketEvent(EXTENDED_TOPIC, "b1"));
        assertReducedColor("red", createExtendedObject("test1", "base1Value", null, "red.dark", "extended1Value", 100));
        assertReducedColor("green", createExtendedObject("test2", "", null ,"green", "some text value", 50));
        assertUnfiltered(new EndBasketEvent());

        assertUnfiltered(new EndTransferEvent());
    }

    @Test
    public void reduceToBaseModelRemovesAdditionalAttributes() throws IoxException {
        List<Model> models = Collections.singletonList((Model) td.getElement(BASE_MODEL));
        reduceToBaseModel = new ReduceToBaseModel(models, td, settings);

        assertUnfiltered(new StartTransferEvent());
        assertBasketUnfiltered(CATALOGUE_TOPIC, "b1",
                createObjectWithName(CATALOGUE_CLASS, "cat1", "Name 1"),
                createObjectWithName(CATALOGUE_CLASS, "cat2", "Name 2"));

        assertUnfiltered(new StartBasketEvent(EXTENDED_TOPIC, "b2"));
        assertReducedToBaseModel(createExtendedObject("test1", "base1Value", "cat1", "green", "extended1Value", 100));
        assertReducedToBaseModel(createExtendedObject("test2", "", "cat2", "blue", "some text value", 50));
        assertReducedToBaseModelClassB(createObjectWithName(EXTENDED_CLASS_B, "testExtendedB1", "B1"));
        assertReducedToBaseModelClassB(createObjectWithName(EXTENDED_CLASS_B, "testExtendedB2", "B2"));
        assertUnfiltered(new EndBasketEvent());

        assertUnfiltered(new EndTransferEvent());
    }

    @Test
    public void reduceToBaseModelKeepsClassesFromBaseModel() throws IoxException {
        List<Model> models = Collections.singletonList((Model) td.getElement(BASE_MODEL));
        reduceToBaseModel = new ReduceToBaseModel(models, td, settings);

        assertUnfiltered(new StartTransferEvent());

        assertBasketUnfiltered(BASE_TOPIC, "b1",
                createObjectWithName(BASE_CLASS_B, "testB1", "B1"),
                createObjectWithName(BASE_CLASS_B, "testB2", "B2"));

        assertUnfiltered(new EndTransferEvent());
    }

    @Test
    public void reduceToBaseModelRemovesClassesFromExtendedModel() throws IoxException {
        List<Model> models = Collections.singletonList((Model) td.getElement(BASE_MODEL));
        reduceToBaseModel = new ReduceToBaseModel(models, td, settings);

        assertUnfiltered(new StartTransferEvent());

        assertUnfiltered(new StartBasketEvent(EXTENDED_TOPIC, "b1"));
        assertRemoved(new ObjectEvent(createObjectWithName(EXTENDED_CLASS_C, "testC1", "C1")));
        assertRemoved(new ObjectEvent(createObjectWithName(EXTENDED_CLASS_C, "testC2", "C2")));
        assertUnfiltered(new EndBasketEvent());

        assertUnfiltered(new EndTransferEvent());
    }

    @Test
    public void reduceToExtendedModelIsUnchanged() throws IoxException {
        List<Model> models = Collections.singletonList((Model) td.getElement(EXTENDED_MODEL));
        reduceToBaseModel = new ReduceToBaseModel(models, td, settings);

        assertUnfiltered(new StartTransferEvent());
        assertBasketUnfiltered(CATALOGUE_TOPIC, "b1",
                createObjectWithName(CATALOGUE_CLASS, "cat1", "Name 1"),
                createObjectWithName(CATALOGUE_CLASS, "cat2", "Name 2"));
        assertBasketUnfiltered(BASE_TOPIC, "b2",
                createObjectWithName(BASE_CLASS_B, "testB1", "B1"),
                createObjectWithName(BASE_CLASS_B, "testB2", "B2"));
        assertBasketUnfiltered(EXTENDED_TOPIC, "b3",
                createExtendedObject("test1", "base1Value", "cat1", "red.dark", "extended1Value", 100),
                createExtendedObject("test2", "", "cat2", "green", "some text value", 50),
                createObjectWithName(EXTENDED_CLASS_B, "testExtendedB1", "B1"),
                createObjectWithName(EXTENDED_CLASS_B, "testExtendedB2", "B2"),
                createObjectWithName(EXTENDED_CLASS_C, "testC1", "C1"),
                createObjectWithName(EXTENDED_CLASS_C, "testC2", "C2"));
        assertUnfiltered(new EndTransferEvent());
    }

    private IomObject createExtendedObject(String oid, String base1, String catalogueRef, String color, String extended1, int extended2) {
        IomObject obj = new Iom_jObject(EXTENDED_CLASS, oid);
        obj.setattrvalue("attrBase1", base1);
        if (catalogueRef != null) {
            IomObject reference = new Iom_jObject(Iom_jObject.REF, null);
            reference.setobjectrefoid(catalogueRef);
            IomObject refObj = new Iom_jObject(CATALOGUE_CLASS_REF, null);
            refObj.addattrobj("Reference", reference);
            obj.addattrobj("attrBase2", refObj);
        }
        obj.setattrvalue("attrBase3", color);
        obj.setattrvalue("attrExtended1", extended1);
        obj.setattrvalue("attrExtended2", Integer.toString(extended2));
        return obj;
    }

    private IomObject createObjectWithName(String className, String oid, String name) {
        IomObject obj = new Iom_jObject(className, oid);
        obj.setattrvalue("name", name);
        return obj;
    }

    private void assertBasketUnfiltered(String topic, String baskedId, IomObject... objects) throws IoxException {
        assertUnfiltered(new StartBasketEvent(topic, baskedId));
        for (IomObject object : objects) {
            assertObjectUnfiltered(object);
        }
        assertUnfiltered(new EndBasketEvent());
    }

    private void assertRemoved(IoxEvent event) throws IoxException {
        assertNull(reduceToBaseModel.filter(event));
    }

    private void assertUnfiltered(IoxEvent event) throws IoxException {
        assertNotNull(reduceToBaseModel.filter(event));
    }

    private void assertObjectUnfiltered(IomObject obj) throws IoxException {
        IoxEvent filteredEvent = reduceToBaseModel.filter(new ObjectEvent(new Iom_jObject(obj)));
        assertNotNull(filteredEvent);
        IomObject filteredObj = ((ObjectEvent) filteredEvent).getIomObject();
        assertEquals(obj.getobjecttag(), filteredObj.getobjecttag());
        assertEqualAttributes(obj, filteredObj);
    }

    private void assertEqualAttributes(IomObject a, IomObject b) {
        assertEquals("Filtered object has a different attribute count", a.getattrcount(), b.getattrcount());
        for (int i = 0; i < a.getattrcount(); i++) {
            String attrName = a.getattrname(i);
            assertEquals("Attribute " + attrName + " is not equal", a.getattrvalue(attrName), b.getattrvalue(attrName));
        }
    }

    private void assertReducedToBaseModelClassB(IomObject extendedObj) throws IoxException {
        IoxEvent filteredEvent = reduceToBaseModel.filter(new ObjectEvent(new Iom_jObject(extendedObj)));
        assertNotNull(filteredEvent);

        IomObject baseObj = ((ObjectEvent) filteredEvent).getIomObject();
        assertEquals(BASE_CLASS_B, baseObj.getobjecttag());
        assertEqualAttributes(extendedObj, baseObj);
    }

    private void assertReducedToBaseModel(IomObject extendedObj) throws IoxException {
        IoxEvent filteredEvent = reduceToBaseModel.filter(new ObjectEvent(new Iom_jObject(extendedObj)));
        assertNotNull(filteredEvent);

        IomObject baseObj = ((ObjectEvent) filteredEvent).getIomObject();
        assertEquals(BASE_CLASS, baseObj.getobjecttag());

        // Filter keeps base attributes
        assertEquals(extendedObj.getattrvalue("attrBase1"), baseObj.getattrvalue("attrBase1"));
        assertNotNull(baseObj.getattrobj("attrBase2", 0));
        assertEquals(extendedObj.getattrobj("attrBase2", 0).getobjectrefoid(), baseObj.getattrobj("attrBase2", 0).getobjectrefoid());

        // Filter removes extended attributes
        assertNull(baseObj.getattrvalue("attrExtended1"));
        assertNull(baseObj.getattrvalue("attrExtended2"));
    }

    private void assertReducedColor(String expectedColor, IomObject extendedObj) throws IoxException {
        IoxEvent filteredEvent = reduceToBaseModel.filter(new ObjectEvent(new Iom_jObject(extendedObj)));
        assertNotNull(filteredEvent);

        IomObject baseObj = ((ObjectEvent) filteredEvent).getIomObject();
        assertEquals(BASE_CLASS, baseObj.getobjecttag());

        assertEquals(expectedColor, baseObj.getattrvalue("attrBase3"));
    }
}
