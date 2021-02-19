package com.adaptris.monitor.agent;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UDPPollerTest {
    @Test
    public void testConstructor() {
        assertTrue((new UDPPoller()).getDatagramReceiver() instanceof UDPDatagramReceiverImpl);
    }

    @Test
    public void testSetDatagramReceiver() {
        UDPPoller udpPoller = new UDPPoller();
        UDPDatagramReceiverImpl udpDatagramReceiverImpl = new UDPDatagramReceiverImpl();
        udpPoller.setDatagramReceiver(udpDatagramReceiverImpl);
        assertSame(udpDatagramReceiverImpl, udpPoller.getDatagramReceiver());
    }
}

