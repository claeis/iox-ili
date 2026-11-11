package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxLogEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.interlis.iox_j.validator.LogCollectorAssertions.AssertAllEventMessages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AreAreasWithoutGaps24Test {
    private static final String ILI_FILE = "src/test/data/validator/AreAreasWithoutGaps24.ili";
    private static final String TOPIC = "AreAreasWithoutGaps24.Topic";
    private static final String CLASS_DIRECT = TOPIC + ".Direct";
    private static final String CLASS_BAG = TOPIC + ".Bag";
    private static final String CLASS_SINGLE = TOPIC + ".Single";
    private static final String CLASS_PLAIN = TOPIC + ".Plain";
    private static final String DIRECT_CONSTRAINT_FAILED = "Set Constraint AreAreasWithoutGaps24.Topic.Direct.Constraint1 is not true.";
    private static final String BAG_CONSTRAINT_FAILED = "Set Constraint AreAreasWithoutGaps24.Topic.Bag.Constraint1 is not true.";
    private static final String SINGLE_CONSTRAINT_FAILED = "Mandatory Constraint AreAreasWithoutGaps24.Topic.Single.Constraint1 is not true.";

    private TransferDescription td;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
    }

    @Test
    public void direct_adjacentSquares_Ok() {
        LogCollector logger = validate(
                direct("o1", rect(0, 0, 10, 10)),
                direct("o2", rect(10, 0, 20, 10)));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void direct_separateIslandsAndTouchingCorner_Ok() {
        LogCollector logger = validate(
                direct("o1", rect(0, 0, 10, 10)),
                direct("o2", rect(10, 10, 20, 20)),
                direct("o3", rect(50, 50, 60, 60)));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void direct_gridWithoutCenter_Fail() {
        LogCollector logger = validate(gridWithoutCenter());

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 10.000), tids o01/Geometry[1], o10/Geometry[1], o12/Geometry[1], o21/Geometry[1]",
                DIRECT_CONSTRAINT_FAILED);
        IoxLogEvent gapEvent = logger.getErrs().get(0);
        assertEquals(10.0, gapEvent.getGeomC1(), 0.0);
        assertEquals(10.0, gapEvent.getGeomC2(), 0.0);
    }

    @Test
    public void direct_enclaveNotFilled_Fail() {
        LogCollector logger = validate(
                direct("o1", rect(0, 0, 30, 30), rect(10, 10, 20, 20)));

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 10.000), tid o1/Geometry[1]",
                DIRECT_CONSTRAINT_FAILED);
    }

    @Test
    public void direct_enclaveFilled_Ok() {
        LogCollector logger = validate(
                direct("o1", rect(0, 0, 30, 30), rect(10, 10, 20, 20)),
                direct("o2", rect(10, 10, 20, 20)));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void direct_sliver_Fail() {
        LogCollector logger = validate(
                direct("o1", rect(0, 1, 10, 11)),
                direct("o2", rect(10.001, 1, 20, 11)),
                direct("o3", IomObjectHelper.createBoundary(c(0, 0), c(20, 0), c(20, 1), c(10.001, 1), c(10, 1), c(0, 1), c(0, 0))),
                direct("o4", IomObjectHelper.createBoundary(c(0, 11), c(10, 11), c(10.001, 11), c(20, 11), c(20, 12), c(0, 12), c(0, 11))));

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 1.000), tids o1/Geometry[1], o2/Geometry[1], o3/Geometry[1], o4/Geometry[1]",
                DIRECT_CONSTRAINT_FAILED);
    }

    @Test
    public void direct_gapTouchingOutsideInOnePoint_Fail() {
        LogCollector logger = validate(
                direct("o1", IomObjectHelper.createBoundary(c(0, 0), c(20, 0), c(20, 10), c(10, 10), c(10, 20), c(0, 20), c(0, 0))),
                direct("o2", IomObjectHelper.createBoundary(c(20, 0), c(30, 0), c(30, 20), c(20, 20), c(20, 10), c(20, 0))),
                direct("o3", IomObjectHelper.createBoundary(c(0, 20), c(10, 20), c(20, 20), c(20, 30), c(0, 30), c(0, 20))));

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 10.000), tids o1/Geometry[1], o2/Geometry[1], o3/Geometry[1]",
                DIRECT_CONSTRAINT_FAILED);
    }

    @Test
    public void direct_sharedArc_Ok() {
        LogCollector logger = validate(
                direct("o1", IomObjectHelper.createBoundary(c(0, 0), c(10, 0), a(12, 5, 10, 10), c(0, 10), c(0, 0))),
                direct("o2", IomObjectHelper.createBoundary(c(10, 0), c(20, 0), c(20, 10), c(10, 10), a(12, 5, 10, 0))));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void direct_lensBetweenArcAndStraight_Fail() {
        LogCollector logger = validate(
                direct("o1", IomObjectHelper.createBoundary(c(0, 0), c(10, 0), a(8, 5, 10, 10), c(0, 10), c(0, 0))),
                direct("o2", rect(10, 0, 20, 10)));

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 0.000), tids o1/Geometry[1], o2/Geometry[1]",
                DIRECT_CONSTRAINT_FAILED);
    }

    @Test
    public void direct_overlap_OnlyAreAreasMessages() {
        // the gap check needs a valid area partition; with an overlap only the areAreas messages are reported
        List<IomObject> objects = new ArrayList<IomObject>();
        for (IomObject object : gridWithoutCenter()) {
            objects.add(object);
        }
        objects.add(direct("o99", rect(5, 5, 15, 15)));

        LogCollector logger = validate(objects.toArray(new IomObject[objects.size()]));

        LogCollectorAssertions.AssertContainsError("Gap coord", 0, logger);
        LogCollectorAssertions.AssertContainsError(DIRECT_CONSTRAINT_FAILED, 1, logger);
        assertTrue(logger.getErrs().size() > 1);
    }

    @Test
    public void bag_twoAdjacentPartsInOneObject_Ok() {
        LogCollector logger = validate(
                bag("o1", rect(0, 0, 10, 10), rect(10, 0, 20, 10)));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void bag_gridWithoutCenter_Fail() {
        LogCollector logger = validate(
                bag("o1", rect(0, 0, 10, 10), rect(10, 0, 20, 10), rect(20, 0, 30, 10)),
                bag("o2", rect(0, 10, 10, 20), rect(20, 10, 30, 20)),
                bag("o3", rect(0, 20, 10, 30), rect(10, 20, 20, 30), rect(20, 20, 30, 30)));

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 10.000), tids o1/Parts[2]/Surface[1], o2/Parts[1]/Surface[1], o2/Parts[2]/Surface[1], o3/Parts[2]/Surface[1]",
                BAG_CONSTRAINT_FAILED);
    }

    @Test
    public void bag_enclaveFilledByOtherObject_Ok() {
        LogCollector logger = validate(
                bagWithEnclave("o1", rect(0, 0, 30, 30), rect(10, 10, 20, 20)),
                bag("o2", rect(10, 10, 20, 20)));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void bag_enclaveNotFilled_Fail() {
        LogCollector logger = validate(
                bagWithEnclave("o1", rect(0, 0, 30, 30), rect(10, 10, 20, 20)));

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 10.000), tid o1/Parts[1]/Surface[1]",
                BAG_CONSTRAINT_FAILED);
    }

    @Test
    public void single_partsWithoutGap_Ok() {
        LogCollector logger = validate(
                single("o1", rect(0, 0, 10, 10), rect(10, 0, 20, 10)),
                // the parts of each object are checked on their own: no gap between the objects
                single("o2", rect(50, 0, 60, 10)));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void single_gridWithoutCenter_Fail() {
        LogCollector logger = validate(
                single("o1", rect(0, 0, 10, 10), rect(10, 0, 20, 10), rect(20, 0, 30, 10),
                        rect(0, 10, 10, 20), rect(20, 10, 30, 20),
                        rect(0, 20, 10, 30), rect(10, 20, 20, 30), rect(20, 20, 30, 30)),
                single("o2", rect(50, 0, 60, 10)));

        AssertAllEventMessages(logger.getErrs(),
                "Gap coord (10.000, 10.000), tids o1/Parts[2]/Surface[1], o1/Parts[4]/Surface[1], o1/Parts[5]/Surface[1], o1/Parts[7]/Surface[1]",
                SINGLE_CONSTRAINT_FAILED);
    }

    @Test
    public void plainAreAreas_gridWithoutCenter_Ok() {
        List<IomObject> objects = new ArrayList<IomObject>();
        for (IomObject object : gridWithoutCenter()) {
            IomObject plain = new Iom_jObject(CLASS_PLAIN, object.getobjectoid());
            plain.addattrobj("Geometry", object.getattrobj("Geometry", 0));
            objects.add(plain);
        }

        LogCollector logger = validate(objects.toArray(new IomObject[objects.size()]));

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void disableAreAreasMessages_Fail() {
        ValidationConfig config = new ValidationConfig();
        config.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES, ValidationConfig.TRUE);

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, "b1", config, gridWithoutCenter());

        AssertAllEventMessages(logger.getErrs(), DIRECT_CONSTRAINT_FAILED);
    }

    @Test
    public void areaOverlapValidationWarning_Fail() {
        ValidationConfig config = new ValidationConfig();
        config.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.AREA_OVERLAP_VALIDATION, ValidationConfig.WARNING);

        LogCollector logger = ValidatorTestHelper.validateObjects(td, TOPIC, "b1", config, gridWithoutCenter());

        AssertAllEventMessages(logger.getWarn(),
                "Gap coord (10.000, 10.000), tids o01/Geometry[1], o10/Geometry[1], o12/Geometry[1], o21/Geometry[1]");
        AssertAllEventMessages(logger.getErrs(), DIRECT_CONSTRAINT_FAILED);
    }

    private LogCollector validate(IomObject... objects) {
        return ValidatorTestHelper.validateObjects(td, TOPIC, objects);
    }

    private static IomObject[] gridWithoutCenter() {
        List<IomObject> objects = new ArrayList<IomObject>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (x != 1 || y != 1) {
                    objects.add(direct("o" + x + y, rect(x * 10, y * 10, x * 10 + 10, y * 10 + 10)));
                }
            }
        }
        return objects.toArray(new IomObject[objects.size()]);
    }

    private static IomObject direct(String oid, IomObject... boundaries) {
        IomObject object = new Iom_jObject(CLASS_DIRECT, oid);
        object.addattrobj("Geometry", IomObjectHelper.createPolygonFromBoundaries(boundaries));
        return object;
    }

    /**
     * Creates a Bag object with one part per boundary.
     */
    private static IomObject bag(String oid, IomObject... boundaries) {
        IomObject object = new Iom_jObject(CLASS_BAG, oid);
        for (IomObject boundary : boundaries) {
            object.addattrobj("Parts", part(boundary));
        }
        return object;
    }

    /**
     * Creates a Single object with one part per boundary.
     */
    private static IomObject single(String oid, IomObject... boundaries) {
        IomObject object = new Iom_jObject(CLASS_SINGLE, oid);
        for (IomObject boundary : boundaries) {
            object.addattrobj("Parts", part(boundary));
        }
        return object;
    }

    /**
     * Creates a Bag object with one part, consisting of a shell and holes.
     */
    private static IomObject bagWithEnclave(String oid, IomObject... boundaries) {
        IomObject object = new Iom_jObject(CLASS_BAG, oid);
        object.addattrobj("Parts", part(boundaries));
        return object;
    }

    private static IomObject part(IomObject... boundaries) {
        IomObject part = new Iom_jObject(TOPIC + ".Part", null);
        part.addattrobj("Surface", IomObjectHelper.createPolygonFromBoundaries(boundaries));
        return part;
    }

    private static IomObject rect(double x1, double y1, double x2, double y2) {
        return IomObjectHelper.createRectangleBoundary(Double.toString(x1), Double.toString(y1), Double.toString(x2), Double.toString(y2));
    }

    private static IomObject c(double x, double y) {
        return IomObjectHelper.createCoord(Double.toString(x), Double.toString(y));
    }

    private static IomObject a(double a1, double a2, double x, double y) {
        return IomObjectHelper.createArc(Double.toString(a1), Double.toString(a2), Double.toString(x), Double.toString(y));
    }
}
