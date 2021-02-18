package com.adaptris.monitor.agent.activity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

public class ActivityMapTest {
    @Test
    public void testConstructor() {
        assertEquals("", (new ActivityMap()).toString());
    }

    @Test
    public void testSetAdapters() {
        ActivityMap activityMap = new ActivityMap();
        activityMap.setAdapters(new HashMap<String, BaseActivity>());
        assertEquals("", activityMap.toString());
    }

    @Test
    public void testToString() {
        assertEquals("", (new ActivityMap()).toString());
    }

    @Test
    public void testToString2() {
        HashMap<String, BaseActivity> stringBaseActivityMap = new HashMap<String, BaseActivity>();
        stringBaseActivityMap.put("foo", new AdapterActivity());
        ActivityMap activityMap = new ActivityMap();
        activityMap.setAdapters(stringBaseActivityMap);
        assertEquals("Adapter = null\n", activityMap.toString());
    }

    @Test
    public void testClone() {
        assertEquals("", (new ActivityMap()).clone().toString());
    }

    @Test
    public void testClone2() {
        HashMap<String, BaseActivity> stringBaseActivityMap = new HashMap<String, BaseActivity>();
        stringBaseActivityMap.put("foo", new AdapterActivity());
        ActivityMap activityMap = new ActivityMap();
        activityMap.setAdapters(stringBaseActivityMap);
        assertEquals("Adapter = null\n", activityMap.clone().toString());
    }
}

