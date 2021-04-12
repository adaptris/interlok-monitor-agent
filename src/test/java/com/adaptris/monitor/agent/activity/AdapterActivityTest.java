package com.adaptris.monitor.agent.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;

public class AdapterActivityTest {
    @Test
    public void testConstructor() {
        assertEquals("Adapter = null\n", (new AdapterActivity()).toString());
    }

    @Test
    public void testAddChannelActivity() {
        AdapterActivity adapterActivity = new AdapterActivity();
        adapterActivity.addChannelActivity(new ChannelActivity());
        assertEquals("Adapter = null\n\tChannel = null\n", adapterActivity.toString());
    }

    @Test
    public void testSetChannels() {
        AdapterActivity adapterActivity = new AdapterActivity();
        adapterActivity.setChannels(new HashMap<String, ChannelActivity>());
        assertEquals("Adapter = null\n", adapterActivity.toString());
    }

    @Test
    public void testToString() {
        assertEquals("Adapter = null\n", (new AdapterActivity()).toString());
    }

    @Test
    public void testToString2() {
        HashMap<String, ChannelActivity> stringChannelActivityMap = new HashMap<String, ChannelActivity>();
        stringChannelActivityMap.put("foo", new ChannelActivity());
        AdapterActivity adapterActivity = new AdapterActivity();
        adapterActivity.setChannels(stringChannelActivityMap);
        assertEquals("Adapter = null\n\tChannel = null\n", adapterActivity.toString());
    }

    @Test
    public void testClone() {
        Object actualCloneResult = (new AdapterActivity()).clone();
        String actualUniqueId = ((AdapterActivity) actualCloneResult).getUniqueId();
        assertEquals("Adapter = null\n", actualCloneResult.toString());
        assertNull(actualUniqueId);
    }

    @Test
    public void testClone2() {
        HashMap<String, ChannelActivity> stringChannelActivityMap = new HashMap<String, ChannelActivity>();
        stringChannelActivityMap.put("foo", new ChannelActivity());
        AdapterActivity adapterActivity = new AdapterActivity();
        adapterActivity.setChannels(stringChannelActivityMap);
        Object actualCloneResult = adapterActivity.clone();
        String actualUniqueId = ((AdapterActivity) actualCloneResult).getUniqueId();
        assertEquals("Adapter = null\n\tChannel = null\n", actualCloneResult.toString());
        assertNull(actualUniqueId);
    }

    @Test
    public void testClone3() {
        HashMap<String, ChannelActivity> stringChannelActivityMap = new HashMap<String, ChannelActivity>();
        stringChannelActivityMap.put("foo", new ChannelActivity());
        stringChannelActivityMap.put("foo", new ChannelActivity());
        AdapterActivity adapterActivity = new AdapterActivity();
        adapterActivity.setChannels(stringChannelActivityMap);
        Object actualCloneResult = adapterActivity.clone();
        String actualUniqueId = ((AdapterActivity) actualCloneResult).getUniqueId();
        assertEquals("Adapter = null\n\tChannel = null\n", actualCloneResult.toString());
        assertNull(actualUniqueId);
    }
}

