package com.adaptris.monitor.agent;

import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageListener;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;
import com.adaptris.monitor.agent.multicast.MulticastEventPropagator;

import junit.framework.TestCase;

public class UDPProfilerConsumerTest extends TestCase {
  
  private static final String DEFAULT_MULTICAST_GROUP = "224.0.0.4";
  private static final int DEFAULT_MULTICAST_PORT = 5577;
  
  private UDPProfilerConsumer consumer;
  private UDPConnection connection;
  private UDPPoller poller;
  
  @Mock private AdaptrisMessageListener mockMessageListener;
  
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    
    consumer = new UDPProfilerConsumer();
    connection = new UDPConnection();
    poller = new UDPPoller();
    
    connection.setGroup(DEFAULT_MULTICAST_GROUP);
    connection.setPort(DEFAULT_MULTICAST_PORT);
    
    consumer.setPoller(poller);
    consumer.registerConnection(connection);
    consumer.registerAdaptrisMessageListener(mockMessageListener);
    consumer.setPacketSize(120400);
  }
  
  public void testConsumeActivityMap() throws Exception {
    LifecycleHelper.initAndStart(connection);
    LifecycleHelper.initAndStart(consumer);
    
    try {
      this.sendUdpPing();
      
      Thread.sleep(500);
      
      verify(mockMessageListener).onAdaptrisMessage(any(AdaptrisMessage.class));
      
    } finally {
      LifecycleHelper.stopAndClose(consumer);
      LifecycleHelper.stopAndClose(connection);
    }
  }
  
  private void sendUdpPing() throws Exception {
    ActivityMap activityMap = new ActivityMap();
    activityMap.getAdapters().put("adapter", new AdapterActivity());
    
    MulticastEventPropagator eventPropagator = new MulticastEventPropagator(EventMonitorReceiver.getInstance());
    eventPropagator.propagateProcessEvent(activityMap);
    
    eventPropagator.stopPropagator();
  }

}
