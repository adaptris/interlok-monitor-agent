package com.adaptris.monitor.agent;

import com.adaptris.profiler.MessageProcessStep;

import junit.framework.TestCase;

public class EventMonitorReceiverTest extends TestCase {
  
  private EventMonitorReceiver receiver;
  
  public void setUp() throws Exception {
    receiver = EventMonitorReceiver.getInstance();
  }
  
  public void testEvents() throws Exception {
    receiver.onEvent(new MessageProcessStep());
    
    assertTrue(receiver.getEvents().size() > 0);
    assertTrue(receiver.getEvents().size() == 0);
  }

}
