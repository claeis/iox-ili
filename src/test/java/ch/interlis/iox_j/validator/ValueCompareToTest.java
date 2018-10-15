package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.validator.Value;

import org.junit.Before;
import org.junit.Test;

public class ValueCompareToTest {

    // OID
    private final static String OID1 = "o1";
    private final static String OID2 = "o2";

    // MODEL.TOPIC
    private final static String TOPIC = "ValueCompareTo.Topic";

    // CLASSES
    private final static String CLASSB = TOPIC + ".ClassB";
    private final static String CLASSE = TOPIC + ".ClassE";
    private final static String CLASSG = TOPIC + ".ClassG";
    private final static String CLASSC1 = TOPIC + ".ClassC1";

    // STRUCTURE
    private final static String STRUCTD1 = TOPIC + ".StructD1";
    private final static String STRUCTS = TOPIC + ".StructS";
    private final static String STRUCTS3 = TOPIC + ".SubSubStruct";

    // TD
    private TransferDescription td = null;

    @Before
    public void setUp() throws Exception {
        Configuration ili2cConfig = new Configuration();
        FileEntry iliFile = new FileEntry("src/test/data/validator/ValueCompare.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(iliFile);
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    // ================== SUCCESS ================== //

    @Test
    public void testSubSubStructureAttrName_OK() {
        // FirstObject
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS8 = (Iom_jObject) objStructS3.addattrobj("attrS3", STRUCTS3);
        objStructS8.setattrvalue("attrS8", "S1");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS9 = (Iom_jObject) objStructS4.addattrobj("attrS3", STRUCTS3);
        objStructS9.setattrvalue("attrS8", "S1");

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objE1);

        // SecondObject
        Iom_jObject objE2 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD2 = (Iom_jObject) objE2.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS5 = (Iom_jObject) objStructD2.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS6 = (Iom_jObject) objStructS5.addattrobj("attrS3", STRUCTS3);
        objStructS6.setattrvalue("attrS8", "S1");

        Iom_jObject objStructS7 = (Iom_jObject) objStructD2.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS10 = (Iom_jObject) objStructS7.addattrobj("attrS3", STRUCTS3);
        objStructS10.setattrvalue("attrS8", "S1");

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objE2);
        Value value = new Value(objects);
        assertEquals(0, (int) value.compareTo(new Value(objectsOther)));
    }

    @Test
    public void refernceAttribute_OK() throws Exception {
        Iom_jObject objC1 = new Iom_jObject(CLASSC1, OID2);
        objC1.setattrvalue("attrC1", "C1");

        Iom_jObject objG = new Iom_jObject(CLASSG, OID1);
        Iom_jObject structD1 = (Iom_jObject) objG.addattrobj("attrB2", STRUCTD1);
        Iom_jObject referenceToC1 = (Iom_jObject) structD1.addattrobj("attrD2", "REF");
        referenceToC1.setobjectrefoid(objC1.getobjectoid());

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objC1);

        Iom_jObject objD1 = new Iom_jObject(CLASSC1, OID2);
        objD1.setattrvalue("attrC1", "C1");

        Iom_jObject objG1 = new Iom_jObject(CLASSG, OID1);
        Iom_jObject structD2 = (Iom_jObject) objG1.addattrobj("attrB2", STRUCTD1);
        Iom_jObject referenceToC2 = (Iom_jObject) structD2.addattrobj("attrD2", "REF");
        referenceToC2.setobjectrefoid(objD1.getobjectoid());

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objD1);
        Value value = new Value(objects);
        assertEquals(0, (int) value.compareTo(new Value(objectsOther)));
    }

    @Test
    public void simpleTypeAttributeCount_OK() throws Exception {
        Iom_jObject objA = new Iom_jObject(CLASSB, OID1);
        Iom_jObject structD1 = (Iom_jObject) objA.addattrobj("attrB2", STRUCTD1);
        structD1.setattrvalue("attrD1", "REF");

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objA);

        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);
        Iom_jObject structD2 = (Iom_jObject) objB.addattrobj("attrB2", STRUCTD1);
        structD2.setattrvalue("attrD1", "REF");

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objB);
        Value value = new Value(objects);
        assertEquals(0, (int) value.compareTo(new Value(objectsOther)));
    }

    @Test
    public void refBid_OK() throws Exception {
        Iom_jObject objA = new Iom_jObject(CLASSB, OID1);
        objA.setobjectrefbid(OID1);

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objA);

        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);
        objB.setobjectrefbid(OID1);

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objB);
        Value value = new Value(objects);
        assertEquals(0, (int) value.compareTo(new Value(objectsOther)));
    }
    
    @Test
    public void subSubstructureAttrCount_OK() throws Exception {
        // FirstObject
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS8 = (Iom_jObject) objStructS3.addattrobj("attrS3", STRUCTS3);
        objStructS8.setattrvalue("attrS8", "S1");
        objStructS8.setattrvalue("attrS10", "S1");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS9 = (Iom_jObject) objStructS4.addattrobj("attrS3", STRUCTS3);
        objStructS9.setattrvalue("attrS8", "S1");

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objE1);

        // SecondObject
        Iom_jObject objE2 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD2 = (Iom_jObject) objE2.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS5 = (Iom_jObject) objStructD2.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS6 = (Iom_jObject) objStructS5.addattrobj("attrS3", STRUCTS3);
        objStructS6.setattrvalue("attrS8", "S1");
        objStructS6.setattrvalue("attrS10", "S1");

        Iom_jObject objStructS7 = (Iom_jObject) objStructD2.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS10 = (Iom_jObject) objStructS7.addattrobj("attrS3", STRUCTS3);
        objStructS10.setattrvalue("attrS8", "S1");

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objE2);
        Value value = new Value(objects);
        assertEquals(0, (int) value.compareTo(new Value(objectsOther)));
    }
    
 // ================== FAIL ================== //

    @Test
    public void testSubSubStructureAttrName_Fail() {
        // FirstObject
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS8 = (Iom_jObject) objStructS3.addattrobj("attrS3", STRUCTS3);
        objStructS8.setattrvalue("attrS8", "S1");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD4", STRUCTS);
        Iom_jObject objStructS9 = (Iom_jObject) objStructS4.addattrobj("attrS3", STRUCTS3);
        objStructS9.setattrvalue("attrS8", "S2");
        
        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objE1);

        // SecondObject
        Iom_jObject objE2 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD2 = (Iom_jObject) objE2.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS5 = (Iom_jObject) objStructD2.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS6 = (Iom_jObject) objStructS5.addattrobj("attrS3", STRUCTS3);
        objStructS6.setattrvalue("attrS9", "S1");

        Iom_jObject objStructS7 = (Iom_jObject) objStructD2.addattrobj("attrD4", STRUCTS);
        Iom_jObject objStructS10 = (Iom_jObject) objStructS7.addattrobj("attrS3", STRUCTS3);
        objStructS10.setattrvalue("attrS8", "S2");

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objE2);
        Value value = new Value(objects);
        assertEquals(-1, (int) value.compareTo(new Value(objectsOther)));
    }

    @Test
    public void refBid_Fail() throws Exception {
        Iom_jObject objA = new Iom_jObject(CLASSB, OID1);
        objA.setobjectrefbid(OID1);

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objA);

        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);
        objB.setobjectrefbid(OID2);

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objB);
        Value value = new Value(objects);
        assertEquals(-1, (int) value.compareTo(new Value(objectsOther)));
    }

    @Test
    public void simpleTypeAttributeCount_Fail() throws Exception {
        Iom_jObject objA = new Iom_jObject(CLASSB, OID1);
        Iom_jObject structD1 = (Iom_jObject) objA.addattrobj("attrB9", STRUCTD1);
        structD1.setattrvalue("attrD1", "REF");

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objA);

        Iom_jObject objB = new Iom_jObject(CLASSB, OID1);

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objB);
        Value value = new Value(objects);
        assertEquals(-1, (int) value.compareTo(new Value(objectsOther)));
    }

    @Test
    public void refernceAttribute_Fail() throws Exception {
        Iom_jObject objC1 = new Iom_jObject(CLASSC1, OID2);
        objC1.setattrvalue("attrC1", "C1");

        Iom_jObject objG = new Iom_jObject(CLASSG, OID1);
        Iom_jObject structD1 = (Iom_jObject) objG.addattrobj("attrB2", STRUCTD1);
        Iom_jObject referenceToC1 = (Iom_jObject) structD1.addattrobj("attrD2", "REF");
        referenceToC1.setobjectrefoid(objC1.getobjectoid());

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objG);

        Iom_jObject objD1 = new Iom_jObject(CLASSC1, OID2);
        objD1.setattrvalue("attrC1", "C1");
        Iom_jObject objG1 = new Iom_jObject(CLASSG, OID1);
        Iom_jObject structD2 = (Iom_jObject) objG1.addattrobj("attrB2", STRUCTD1);
        Iom_jObject referenceToC2 = (Iom_jObject) structD2.addattrobj("attrD2", "REF");
        referenceToC2.setobjectrefoid(objD1.getobjectoid());

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objD1);
        Value value = new Value(objects);
        assertEquals(-1, (int) value.compareTo(new Value(objectsOther)));
    }

    @Test
    public void subSubstructureAttrCount_Fail() throws Exception {
        // FirstObject
        Iom_jObject objE1 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD1 = (Iom_jObject) objE1.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS3 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS8 = (Iom_jObject) objStructS3.addattrobj("attrS3", STRUCTS3);
        objStructS8.setattrvalue("attrS8", "S1");
        objStructS8.setattrvalue("attrS9", "S1");

        Iom_jObject objStructS4 = (Iom_jObject) objStructD1.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS9 = (Iom_jObject) objStructS4.addattrobj("attrS3", STRUCTS3);
        objStructS9.setattrvalue("attrS8", "S1");

        List<IomObject> objects = new ArrayList<IomObject>();
        objects.add(objE1);

        // SecondObject
        Iom_jObject objE2 = new Iom_jObject(CLASSE, OID1);
        Iom_jObject objStructD2 = (Iom_jObject) objE2.addattrobj("attrE2", STRUCTD1);
        Iom_jObject objStructS5 = (Iom_jObject) objStructD2.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS6 = (Iom_jObject) objStructS5.addattrobj("attrS3", STRUCTS3);
        objStructS6.setattrvalue("attrS8", "S1");

        Iom_jObject objStructS7 = (Iom_jObject) objStructD2.addattrobj("attrD3", STRUCTS);
        Iom_jObject objStructS10 = (Iom_jObject) objStructS7.addattrobj("attrS3", STRUCTS3);
        objStructS10.setattrvalue("attrS8", "S1");

        List<IomObject> objectsOther = new ArrayList<IomObject>();
        objectsOther.add(objE2);
        Value value = new Value(objects);
        assertEquals(-1, (int) value.compareTo(new Value(objectsOther)));
    }
}
