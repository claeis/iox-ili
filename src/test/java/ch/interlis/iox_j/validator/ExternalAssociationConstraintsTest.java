package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * MANDATORY CONSTRAINTs that navigate an EXTERNAL role, with and without allObjectsAccessible.
 */
public class ExternalAssociationConstraintsTest {
    private static final String ILI_FILE = "src/test/data/validator/ExternalAssociationConstraints.ili";

    private static final String TOPIC = "ExternalAssociationConstraints.T";
    private static final String CLASS_ITEM = TOPIC + ".Item";
    private static final String CLASS_OWNER = TOPIC + ".Owner";
    private static final String CLASS_CONTRACT = TOPIC + ".Contract";
    private static final String ROLE_OWNER_REF = "OwnerRef";
    private static final String ROLE_CONTRACT_OWNER_REF = "ContractOwnerRef";
    private static final String ATTR_CODE = "Code";
    private static final String ATTR_AGE = "Age";
    private static final String BID = "b1";

    private TransferDescription td;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
    }

    @Test
    public void referencedObjectNotAccessible_allObjectsNotAccessible_constraintSkipped() {
        Iom_jObject item = newItem("i1", "abc", "externalOwner1");

        LogCollector logger = validate(false, item);

        assertEquals(0, logger.getErrs().size());
        LogCollectorAssertions.AssertContainsInfo("ExternalAssociationConstraints.T.Item.OwnerAgeDefined not evaluated, target object of external role ExternalAssociationConstraints.T.Ownership.OwnerRef is not accessible.", 1, logger);
        LogCollectorAssertions.AssertContainsInfo("ExternalAssociationConstraints.T.Item.OwnerAdult not evaluated, target object of external role ExternalAssociationConstraints.T.Ownership.OwnerRef is not accessible.", 1, logger);
    }

    @Test
    public void referencedObjectNotAccessible_allObjectsNotAccessible_infoLoggedOncePerConstraint() {
        Iom_jObject item1 = newItem("i1", "abc", "externalOwner1");
        Iom_jObject item2 = newItem("i2", "def", "externalOwner2");
        Iom_jObject item3 = newItem("i3", "ghi", "externalOwner1");

        LogCollector logger = validate(false, item1, item2, item3);

        assertEquals(0, logger.getErrs().size());
        LogCollectorAssertions.AssertContainsInfo("ExternalAssociationConstraints.T.Item.OwnerAgeDefined not evaluated, target object of external role ExternalAssociationConstraints.T.Ownership.OwnerRef is not accessible.", 1, logger);
        LogCollectorAssertions.AssertContainsInfo("ExternalAssociationConstraints.T.Item.OwnerAdult not evaluated, target object of external role ExternalAssociationConstraints.T.Ownership.OwnerRef is not accessible.", 1, logger);
    }

    @Test
    public void referencedObjectNotAccessible_allObjectsNotAccessible_associationConstraintSkipped() {
        Iom_jObject contract = newContract("c1", "abc", "externalOwner1");

        LogCollector logger = validate(false, contract);

        assertEquals(0, logger.getErrs().size());
        LogCollectorAssertions.AssertContainsInfo("ExternalAssociationConstraints.T.ContractOwnership.ContractOwnerAgeDefined not evaluated, target object of external role ExternalAssociationConstraints.T.ContractOwnership.ContractOwnerRef is not accessible.", 1, logger);
    }

    @Test
    public void referencedObjectNotAccessible_allObjectsAccessible_referenceReported() {
        Iom_jObject item = newItem("i1", "abc", "externalOwner1");

        LogCollector logger = validate(true, item);

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "No object found with OID externalOwner1.",
                "Mandatory Constraint ExternalAssociationConstraints.T.Item.OwnerAgeDefined is not true.");
    }

    @Test
    public void referencedObjectAccessible_ageDefined_ok() {
        Iom_jObject owner = new Iom_jObject(CLASS_OWNER, "ow1");
        owner.setattrvalue(ATTR_AGE, "20");
        Iom_jObject item = newItem("i1", "abc", "ow1");
        Iom_jObject contract = newContract("c1", "abc", "ow1");

        LogCollector logger = validate(false, owner, item, contract);

        assertEquals(0, logger.getErrs().size());
    }

    @Test
    public void referencedObjectAccessible_ageUndefined_constraintViolated() {
        Iom_jObject owner = new Iom_jObject(CLASS_OWNER, "ow1");
        Iom_jObject item = newItem("i1", "abc", "ow1");
        Iom_jObject contract = newContract("c1", "abc", "ow1");

        LogCollector logger = validate(false, owner, item, contract);

        LogCollectorAssertions.AssertAllEventMessages(logger.getErrs(),
                "Mandatory Constraint ExternalAssociationConstraints.T.Item.OwnerAgeDefined is not true.",
                "Mandatory Constraint ExternalAssociationConstraints.T.ContractOwnership.ContractOwnerAgeDefined is not true.");
    }

    @Test
    public void referencedObjectAccessible_ageBelow18_constraintViolated() {
        Iom_jObject owner = new Iom_jObject(CLASS_OWNER, "ow1");
        owner.setattrvalue(ATTR_AGE, "10");
        Iom_jObject item = newItem("i1", "abc", "ow1");

        LogCollector logger = validate(false, owner, item);

        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ExternalAssociationConstraints.T.Item.OwnerAdult is not true.", logger.getErrs().get(0).getEventMsg());
    }

    private LogCollector validate(boolean allObjectsAccessible, Iom_jObject... objects) {
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.setConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE, allObjectsAccessible ? ValidationConfig.TRUE : ValidationConfig.FALSE);
        return ValidatorTestHelper.validateObjects(td, TOPIC, BID, modelConfig, objects);
    }

    private Iom_jObject newItem(String oid, String code, String ownerOid) {
        Iom_jObject item = new Iom_jObject(CLASS_ITEM, oid);
        item.setattrvalue(ATTR_CODE, code);
        item.addattrobj(ROLE_OWNER_REF, "REF").setobjectrefoid(ownerOid);
        return item;
    }

    private Iom_jObject newContract(String oid, String code, String ownerOid) {
        Iom_jObject contract = new Iom_jObject(CLASS_CONTRACT, oid);
        contract.setattrvalue(ATTR_CODE, code);
        contract.addattrobj(ROLE_CONTRACT_OWNER_REF, "REF").setobjectrefoid(ownerOid);
        return contract;
    }
}
