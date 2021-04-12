package com.adaptris.monitor.agent.json;

import static org.junit.Assert.assertSame;

import com.adaptris.monitor.agent.activity.ActivityMap;
import org.junit.Test;

public class EventJsonMarshallerTest {
    @Test
    public void testActivtyWrapperConstructor() {
        EventJsonMarshaller eventJsonMarshaller = new EventJsonMarshaller();
        ActivityMap activityMap = new ActivityMap();
//        assertSame((eventJsonMarshaller.new ActivtyWrapper(activityMap)).adapters, activityMap);
    }
}

