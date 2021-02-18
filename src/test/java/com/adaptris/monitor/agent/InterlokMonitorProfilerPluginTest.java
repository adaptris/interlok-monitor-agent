package com.adaptris.monitor.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.adaptris.profiler.client.EventReceiver;

import java.util.List;

import org.junit.Test;

public class InterlokMonitorProfilerPluginTest {
    @Test
    public void testConstructor() {
        List<EventReceiver> receivers = (new InterlokMonitorProfilerPlugin()).getReceivers();
        assertTrue(receivers instanceof java.util.ArrayList);
        assertEquals(1, receivers.size());
    }

    @Test
    public void testGetReceivers() {
        assertEquals(1, (new InterlokMonitorProfilerPlugin()).getReceivers().size());
    }
}

