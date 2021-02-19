package com.adaptris.monitor.agent.jmx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProfilerEventClientTest {
    @Test
    public void testConstructor() throws Exception {
        ProfilerEventClient actualProfilerEventClient = new ProfilerEventClient();
        assertEquals(0, actualProfilerEventClient.getMaxEventHistory());
        assertEquals(0, actualProfilerEventClient.getEventCount());
    }

    @Test
    public void testSetMaxEventHistory() {
        ProfilerEventClient profilerEventClient = new ProfilerEventClient();
        profilerEventClient.setMaxEventHistory(3);
        assertEquals(3, profilerEventClient.getMaxEventHistory());
    }

    @Test
    public void testMaxEventHistory() {
        assertEquals(100, (new ProfilerEventClient()).maxEventHistory());
    }

    @Test
    public void testGetEventQueue() throws Exception {
        ProfilerEventClient profilerEventClient = new ProfilerEventClient();
        assertEquals(0, profilerEventClient.getEventCache().size());
        assertEquals(0, profilerEventClient.getEventCount());
    }

    @Test
    public void testGetEventQueue2() throws Exception {
        ProfilerEventClient profilerEventClient = new ProfilerEventClient();
        profilerEventClient.setMaxEventHistory(3);
        assertEquals(0, profilerEventClient.getEventCache().size());
        assertEquals(0, profilerEventClient.getEventCount());
    }

    @Test
    public void testGetEventCount() throws Exception {
        ProfilerEventClient profilerEventClient = new ProfilerEventClient();
        assertEquals(0, profilerEventClient.getEventCount());
        assertEquals(0, profilerEventClient.getEventCount());
    }

    @Test
    public void testGetEventActivityMap() throws Exception {
        ProfilerEventClient profilerEventClient = new ProfilerEventClient();
        assertEquals(0, profilerEventClient.getEventActivityMaps().size());
        assertEquals(0, profilerEventClient.getEventCount());
    }
}

