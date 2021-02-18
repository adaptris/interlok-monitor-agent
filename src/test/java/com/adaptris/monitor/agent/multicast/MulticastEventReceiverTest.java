package com.adaptris.monitor.agent.multicast;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adaptris.monitor.agent.EventReceiverListener;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;

public class MulticastEventReceiverTest {

  private static final String DEFAULT_MULTICAST_GROUP = "224.0.0.4";
  private static final int DEFAULT_MULTICAST_PORT = 5577;
  private static final int STANDARD_PACKET_SIZE = 120400;

  private MulticastEventReceiver receiver;

  @Mock MulticastSocketReceiver mockReceiver;

  private final Object monitor = new Object();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    receiver = new MulticastEventReceiver();
    receiver.setMulticastSocketReceiver(mockReceiver);
  }

  @Test
  public void testReceive() throws Exception {    
    when(mockReceiver.receive(STANDARD_PACKET_SIZE))
        .thenReturn(buildPacket());

    receiver.addEventReceiverListener(new EventReceiverListener() {
      
      @Override
      public void eventReceived(ActivityMap activityMap) {
        monitor.notifyAll();
      }
    });
    receiver.start();

    synchronized(monitor) {
      // Wait at most 10 seconds... since multicast doesn't always work.
      monitor.wait(TimeUnit.SECONDS.toMillis(10L));
    }
    receiver.stop();
  }
  
    
    @Test
    public void testConstructor() {
        MulticastEventReceiver actualMulticastEventReceiver = new MulticastEventReceiver();
        assertTrue(actualMulticastEventReceiver.getMulticastSocketReceiver() instanceof MulticastSocketReceiverImpl);
        assertTrue(actualMulticastEventReceiver.log instanceof org.slf4j.impl.SimpleLogger);
    }

  private DatagramPacket buildPacket() throws Exception {
    ActivityMap activityMap = new ActivityMap();
    activityMap.getAdapters().put("adapter", new AdapterActivity());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(activityMap);
    oos.flush();
    byte[] data= baos.toByteArray();

    return new DatagramPacket(data, data.length, InetAddress.getByName(DEFAULT_MULTICAST_GROUP), DEFAULT_MULTICAST_PORT);
  }
}