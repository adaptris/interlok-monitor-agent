package com.adaptris.monitor.agent.activity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

public class ChannelActivityTest {
    @Test
    public void testConstructor() {
        assertEquals("\tChannel = null\n", (new ChannelActivity()).toString());
    }

    @Test
    public void testSetWorkflows() {
        ChannelActivity channelActivity = new ChannelActivity();
        channelActivity.setWorkflows(new HashMap<String, WorkflowActivity>());
        assertEquals("\tChannel = null\n", channelActivity.toString());
    }
}

