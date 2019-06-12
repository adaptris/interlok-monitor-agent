package com.adaptris.monitor.agent.multicast;

import org.mockito.MockitoAnnotations;

import com.adaptris.monitor.agent.EventMonitorReceiver;
import com.adaptris.monitor.agent.EventReceiverListener;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;

import junit.framework.TestCase;

public class MulticastEventReceiverTest extends TestCase {
  
  private MulticastEventReceiver receiver;
  
  private final Object monitor = new Object();
    
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    
    receiver = new MulticastEventReceiver();
  }
  
  public void testReceive() throws Exception {    
    receiver.addEventReceiverListener(new EventReceiverListener() {
      @Override
      public void eventReceived(ActivityMap activityMap) {
        synchronized(monitor) {
          monitor.notifyAll();
        }
      }
    });
    receiver.start();
    
    sendUdpPing();
    
    synchronized(monitor) {
      monitor.wait();
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
