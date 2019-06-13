package com.adaptris.monitor.agent.multicast;

import java.util.concurrent.TimeUnit;

import org.mockito.MockitoAnnotations;

import com.adaptris.monitor.agent.EventMonitorReceiver;
import com.adaptris.monitor.agent.EventReceiverListener;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;

import junit.framework.TestCase;

public class MulticastEventReceiverTest extends TestCase {
  
  private MulticastEventReceiver receiver;
  
  private final Object monitor = new Object();
    
  @Override
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    
    receiver = new MulticastEventReceiver();
  }
  
  public void testReceive() throws Exception {    
    receiver.addEventReceiverListener(new EventReceiverListener() {
      @Override
      public void eventReceived(ActivityMap activityMap) {
          monitor.notifyAll();
      }
    });
    receiver.start();
    
    sendUdpPing();
    
    synchronized(monitor) {
      // Wait at most 10 seconds... since multicast doesn't always work.
      monitor.wait(TimeUnit.SECONDS.toMillis(10L));
    }
    receiver.stop();
  }
  
  private void sendUdpPing() throws Exception {
    ActivityMap activityMap = new ActivityMap();
    activityMap.getAdapters().put("adapter", new AdapterActivity());
    
    MulticastEventPropagator eventPropagator = new MulticastEventPropagator(EventMonitorReceiver.getInstance());
    eventPropagator.propagateProcessEvent(activityMap);
    
    eventPropagator.stopPropagator();
  }
}
