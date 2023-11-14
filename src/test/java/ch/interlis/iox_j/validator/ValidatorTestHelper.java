package ch.interlis.iox_j.validator;

import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Assert;

import static org.junit.Assert.assertNotNull;

public class ValidatorTestHelper {
    public static TransferDescription compileIliFile(String filename) {
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(filename, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        try {
            td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        } catch (Ili2cFailure e) {
            Assert.fail("Could not compile ili file <" + filename + ">: " + e.getMessage());
        }
        assertNotNull(td);

        return td;
    }
}
