package ch.interlis.iox_j.validator;

import ch.interlis.iox.IoxLogEvent;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogCollectorAssertions {
    public static void AssertContainsError(String text, int count, LogCollector logCollector){
        assertEquals( String.format("Wrong number of error logs containing <%s> found.", text), count, CountOccurrences(text, logCollector.getErrs()));
    }

    public static void AssertContainsWarning(String text, int count, LogCollector logCollector){
        assertEquals(String.format("Wrong number of warning logs containing <%s> found.", text), count, CountOccurrences(text, logCollector.getWarn()));
    }

    public static void AssertContainsInfo(String text, int count, LogCollector logCollector){
        assertEquals(String.format("Wrong number of info logs containing <%s> found.", text), count, CountOccurrences(text, logCollector.getInfo()));
    }

    private static int CountOccurrences(String text, List<IoxLogEvent> logEvents){
        int i = 0;
        for (IoxLogEvent event : logEvents) {
            if (event.getEventMsg().contains(text)){
                i++;
            }
        }
        return i;
    }
}
