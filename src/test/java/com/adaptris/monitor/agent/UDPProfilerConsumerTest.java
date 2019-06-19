package com.adaptris.monitor.agent;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageListener;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;

import junit.framework.TestCase;

public class UDPProfilerConsumerTest extends TestCase {
  
  private static final String DEFAULT_MULTICAST_GROUP = "224.0.0.4";
  private static final int DEFAULT_MULTICAST_PORT = 5577;
  
  private UDPProfilerConsumer consumer;
  private UDPConnection connection;
  private UDPPoller poller;
  
  @Mock private AdaptrisMessageListener mockMessageListener;
  @Mock private UDPDatagramReceiver mockDatagramReceiver;
  
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    
    consumer = new UDPProfilerConsumer();
    connection = new UDPConnection();
    poller = new UDPPoller();
    poller.setDatagramReceiver(mockDatagramReceiver);
    
    connection.setGroup(DEFAULT_MULTICAST_GROUP);
    connection.setPort(DEFAULT_MULTICAST_PORT);
    
    consumer.setPoller(poller);
    consumer.registerConnection(connection);
    consumer.registerAdaptrisMessageListener(mockMessageListener);
    consumer.setPacketSize(120400);
  }
  
  public void testConsumeActivityMap() throws Exception {
    when(mockDatagramReceiver.receive(consumer))
        .thenReturn(buildPacket());
    
    LifecycleHelper.initAndStart(connection);
    LifecycleHelper.initAndStart(consumer);
    
    try {
      Thread.sleep(500);
      
      verify(mockMessageListener, atLeast(1)).onAdaptrisMessage(any(AdaptrisMessage.class));
      
    } finally {
      LifecycleHelper.stopAndClose(consumer);
      LifecycleHelper.stopAndClose(connection);
    }
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
