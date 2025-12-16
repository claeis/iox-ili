package ch.interlis.iox_j.filter;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReduceToBaseModelTranslationTest {
    private static final String ILI_FILE = "src/test/data/filter/ReduceToBaseModelTranslationTest.ili";
    private static final String EN_MODEL = "ModelEN";
    private static final String EN_TOPIC = EN_MODEL + ".TopicEN";
    private static final String EN_CLASS = EN_TOPIC + ".ClassEN";
    private static final String DE_MODEL = "ModelDE";
    private static final String DE_TOPIC = DE_MODEL + ".TopicDE";
    private static final String DE_CLASS = DE_TOPIC + ".ClassDE";

    private static final Map<String, String> ENUM_VALUES_EN_TO_DE = new HashMap<String, String>() {{
        put("red.dark_red", "rot.dunkelrot");
        put("red.orange", "rot.orange");
        put("red.crimson", "rot.karmin");
        put("yellow", "gelb");
        put("green.light_green", "gruen.hellgruen");
        put("green.dark_green", "gruen.dunkelgruen");
    }};
    private static final Map<String, String> ENUM_TREE_VALUES_EN_TO_DE = new HashMap<String, String>(ENUM_VALUES_EN_TO_DE) {{
        put("red", "rot");
        put("green", "gruen");
    }};

    private TransferDescription td;
    private ReduceToBaseModel reduceToBaseModel;
    private Settings settings;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
        settings = new Settings();
    }

    @Test
    public void translateAttributeNamesToDE() throws IoxException {
        reduceToBaseModel = setupTranslation(DE_MODEL);

        IomObject objectEN = new Iom_jObject(EN_CLASS, "o1");
        objectEN.setattrvalue("count", "5");
        objectEN.setattrvalue("content", "english text value");

        IomObject objectDE = translate(EN_TOPIC, "b1", objectEN);
        assertEquals(DE_CLASS, objectDE.getobjecttag());
        assertEquals("5", objectDE.getattrvalue("anzahl"));
        assertEquals("english text value", objectDE.getattrvalue("inhalt"));
        assertNull(objectDE.getattrvalue("count"));
        assertNull(objectDE.getattrvalue("content"));
    }

    @Test
    public void translateAttributeNamesToEN() throws IoxException {
        reduceToBaseModel = setupTranslation(EN_MODEL);

        IomObject objectDE = new Iom_jObject(DE_CLASS, "o1");
        objectDE.setattrvalue("anzahl", "5");
        objectDE.setattrvalue("inhalt", "deutscher Textwert");

        IomObject objectEN = translate(DE_TOPIC, "b1", objectDE);
        assertEquals(EN_CLASS, objectEN.getobjecttag());
        assertEquals("5", objectEN.getattrvalue("count"));
        assertEquals("deutscher Textwert", objectEN.getattrvalue("content"));
        assertNull(objectEN.getattrvalue("anzahl"));
        assertNull(objectEN.getattrvalue("inhalt"));
    }

    @Test
    public void translateEnumValuesToDE() throws IoxException {
        reduceToBaseModel = setupTranslation(DE_MODEL);

        int i = 0;
        for (Map.Entry<String, String> entry : ENUM_VALUES_EN_TO_DE.entrySet()) {
            IomObject objectEN = new Iom_jObject(EN_CLASS, "o" + i);
            objectEN.setattrvalue("color", entry.getKey());

            IomObject objectDE = translate(EN_TOPIC, "b" + i, objectEN);
            assertEquals(DE_CLASS, objectDE.getobjecttag());
            assertEquals(entry.getValue(), objectDE.getattrvalue("farbe"));
            assertNull(objectDE.getattrvalue("color"));
            i++;
        }
    }

    @Test
    public void translateEnumValuesToEN() throws IoxException {
        reduceToBaseModel = setupTranslation(EN_MODEL);

        int i = 0;
        for (Map.Entry<String, String> entry : ENUM_VALUES_EN_TO_DE.entrySet()) {
            IomObject objectDE = new Iom_jObject(DE_CLASS, "o" + i);
            objectDE.setattrvalue("farbe", entry.getValue());

            IomObject objectEN = translate(DE_TOPIC, "b" + i, objectDE);
            assertEquals(EN_CLASS, objectEN.getobjecttag());
            assertEquals(entry.getKey(), objectEN.getattrvalue("color"));
            assertNull(objectEN.getattrvalue("farbe"));
            i++;
        }
    }

    @Test
    public void translateEnumTreeValuesToDE() throws IoxException {
        reduceToBaseModel = setupTranslation(DE_MODEL);

        int i = 0;
        for (Map.Entry<String, String> entry : ENUM_TREE_VALUES_EN_TO_DE.entrySet()) {
            IomObject objectEN = new Iom_jObject(EN_CLASS, "o" + i);
            objectEN.setattrvalue("allColors", entry.getKey());

            IomObject objectDE = translate(EN_TOPIC, "b" + i, objectEN);
            assertEquals(DE_CLASS, objectDE.getobjecttag());
            assertEquals(entry.getValue(), objectDE.getattrvalue("alleFarben"));
            assertNull(objectDE.getattrvalue("allColors"));
            i++;
        }
    }

    @Test
    public void translateEnumTreeValuesToEN() throws IoxException {
        reduceToBaseModel = setupTranslation(EN_MODEL);

        int i = 0;
        for (Map.Entry<String, String> entry : ENUM_TREE_VALUES_EN_TO_DE.entrySet()) {
            IomObject objectDE = new Iom_jObject(DE_CLASS, "o" + i);
            objectDE.setattrvalue("alleFarben", entry.getValue());

            IomObject objectEN = translate(DE_TOPIC, "b" + i, objectDE);
            assertEquals(EN_CLASS, objectEN.getobjecttag());
            assertEquals(entry.getKey(), objectEN.getattrvalue("allColors"));
            assertNull(objectEN.getattrvalue("alleFarben"));
            i++;
        }
    }

    private ReduceToBaseModel setupTranslation(String targetModel) {
        List<Model> models = Collections.singletonList((Model) td.getElement(targetModel));
        return new ReduceToBaseModel(models, td, settings);
    }

    private IomObject translate(String sourceBasketType, String bid, IomObject obj) throws IoxException {
        reduceToBaseModel.filter(new StartTransferEvent());
        reduceToBaseModel.filter(new StartBasketEvent(sourceBasketType, bid));
        IomObject translated = translateObject(obj);
        reduceToBaseModel.filter(new EndBasketEvent());
        reduceToBaseModel.filter(new EndTransferEvent());

        return translated;
    }

    private IomObject translateObject(IomObject obj) throws IoxException {
        IomObject copy = new Iom_jObject(obj);
        ObjectEvent event = (ObjectEvent) reduceToBaseModel.filter(new ObjectEvent(copy));
        return event.getIomObject();
    }
}
