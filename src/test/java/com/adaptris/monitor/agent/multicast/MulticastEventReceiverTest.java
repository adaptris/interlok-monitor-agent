package com.adaptris.monitor.agent.multicast;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MulticastEventReceiverTest {
    @Test
    public void testConstructor() {
        MulticastEventReceiver actualMulticastEventReceiver = new MulticastEventReceiver();
        assertTrue(actualMulticastEventReceiver.getMulticastSocketReceiver() instanceof MulticastSocketReceiverImpl);
        assertTrue(actualMulticastEventReceiver.log instanceof org.slf4j.impl.SimpleLogger);
    }
}

