package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.IoxException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class AssociationPathTest {
    private static final String TEST_DIR = "src/test/data/validator";
    private static final String ILI_FILE = TEST_DIR + "/AssociationPath.ili";
    private static final String XTF_FILE = TEST_DIR + "/AssociationPath.xtf";

    private TransferDescription td;

    @Before
    public void setUp() {
        td = ValidatorTestHelper.compileIliFile(ILI_FILE);
    }

    @Test
    public void testAssociationPath() throws IoxException {
        File xtfFile = new File(XTF_FILE);
        LogCollector logger = ValidatorTestHelper.validateObjectsFromXtf24(td, xtfFile);

        assertEquals(1, logger.getErrs().size());
        assertEquals("Mandatory Constraint ModelA.TopicA.ClassA1.IsOfClassAShouldFail is not true.", logger.getErrs().get(0).getEventMsg());
    }
}
