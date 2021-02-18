package com.adaptris.monitor.agent;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InterlokMonitorPluginFactoryTest {
    @Test
    public void testGetPlugin() {
        assertTrue((new InterlokMonitorPluginFactory()).getPlugin() instanceof InterlokMonitorProfilerPlugin);
    }
}

