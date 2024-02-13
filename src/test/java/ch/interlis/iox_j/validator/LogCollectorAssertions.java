package ch.interlis.iox_j.validator;

import ch.interlis.iox.IoxLogEvent;

import java.util.List;

import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LogCollectorAssertions {
    /**
     * Asserts that the given events have exactly the expected messages in any order.
     */
    public static void AssertAllEventMessages(List<IoxLogEvent> events, String... expectedMessages){
        String[] actualMessages = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            actualMessages[i] = events.get(i).getEventMsg();
        }
        assertThat(actualMessages, arrayContainingInAnyOrder(expectedMessages));
    }

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
