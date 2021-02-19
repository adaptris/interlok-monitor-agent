package com.adaptris.monitor.agent.activity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ServiceActivityTest {
    @Test
    public void testConstructor() {
        ServiceActivity actualServiceActivity = new ServiceActivity();
        assertEquals("\t\t\tService = null (-1) (0 at 0  nanos (0 ms))\n", actualServiceActivity.toString());
        assertEquals(-1L, actualServiceActivity.getOrder());
    }
}

