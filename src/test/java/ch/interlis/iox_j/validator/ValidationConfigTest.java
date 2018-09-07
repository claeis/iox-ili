package ch.interlis.iox_j.validator;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class ValidationConfigTest {
    @Test
    public void readQuotedParamName()
    throws Exception
    {
        ValidationConfig config=ValidationConfig.readFromConfigFile(new File("src/test/data/validator/Config.toml"));
        assertEquals("tool.value",config.getConfigValue("model.topic.class", "tool.param"));
        assertEquals("value",config.getConfigValue("model.topic.class", "test"));
    }
}
