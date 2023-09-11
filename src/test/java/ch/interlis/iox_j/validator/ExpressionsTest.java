package ch.interlis.iox_j.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ExpressionsTest {

    private TransferDescription td;
    private LogCollector logger;
    private Validator validator;
    @Before
    public void setUp() throws Exception {
		Configuration ili2cConfig = new Configuration();
		FileEntry fileEntry = new FileEntry("src/test/data/validator/Expressions.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		assertNotNull(td);

        ValidationConfig validationConfig = new ValidationConfig();
		logger=new LogCollector();
		LogEventFactory errFactory = new LogEventFactory();
		Settings settings = new Settings();
		validator = new Validator(td, validationConfig,logger,errFactory,settings);

        validator.validate(new StartTransferEvent());
		validator.validate(new StartBasketEvent("ModelA.TopicA", "b1"));
    }

    private void CloseBasketAndValidate(){
        validator.validate(new EndBasketEvent());
		validator.validate(new EndTransferEvent());
    }

    private void AddClassAObject(Integer attr1, Integer attr2){
        Iom_jObject object = new Iom_jObject("ModelA.TopicA.ClassA", java.util.UUID.randomUUID().toString());
        if(attr1 != null)
            object.addattrvalue("attr1", attr1.toString());

        if (attr2 != null)
            object.addattrvalue("attr2", attr2.toString());

        validator.validate(new ObjectEvent(object));
    }

    private void AssertLogErrorContains(int count, String text){
        int counter = 0;
        for (int i = 0; i < logger.getErrs().size(); i++) {
            if (logger.getErrs().get(i).getEventMsg().contains(text)){
                counter++;
            }
        }
        Assert.assertEquals(String.format("Unexpected number of errors containing <%s>", text), count, counter);
    }

    @Test
    public void Implication_OK(){
        AddClassAObject(10, 10);
        AddClassAObject(20, 20);
        CloseBasketAndValidate();
        AssertLogErrorContains(0, "CheckImplication");
    }

    @Test
    public void Implication_Fail(){
        AddClassAObject(20, 10);
        CloseBasketAndValidate();
        AssertLogErrorContains(1, "CheckImplication");
    }

    @Test
    public void Addition_OK(){
        AddClassAObject(20, 20);
        CloseBasketAndValidate();
        AssertLogErrorContains(0, "CheckAddition");
    }

    @Test
    public void Addition_Fail(){
        AddClassAObject(10, 10);
        CloseBasketAndValidate();
        AssertLogErrorContains(1, "CheckAddition");
    }

    @Test
    public void Subtraction_OK(){
        AddClassAObject(20, 15);
        CloseBasketAndValidate();
        AssertLogErrorContains(0, "CheckSubtraction");
    }

    @Test
    public void Subtraction_Fail(){
        AddClassAObject(20, 10);
        CloseBasketAndValidate();
        AssertLogErrorContains(1, "CheckSubtraction");
    }

    @Test
    public void Multiplication_OK(){
        AddClassAObject(20, 20);
        CloseBasketAndValidate();
        AssertLogErrorContains(0, "CheckMultiplication");
    }

    @Test
    public void Multiplication_Fail(){
        AddClassAObject(10, 10);
        CloseBasketAndValidate();
        AssertLogErrorContains(1, "CheckMultiplication");
    }

    @Test
    public void Division_OK(){
        AddClassAObject(20, 10);
        CloseBasketAndValidate();
        AssertLogErrorContains(0, "CheckDivision");
    }

    @Test
    public void Division_Fail(){
        AddClassAObject(10, 15);
        CloseBasketAndValidate();
        AssertLogErrorContains(1, "CheckDivision");
    }
}
