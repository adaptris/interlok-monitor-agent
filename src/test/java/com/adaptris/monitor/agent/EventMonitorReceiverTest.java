package com.adaptris.monitor.agent;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.adaptris.profiler.MessageProcessStep;

public class EventMonitorReceiverTest {
  
  private EventMonitorReceiver receiver;
  
  @Before
  public void setUp() throws Exception {
    receiver = EventMonitorReceiver.getInstance();
  }
  
  @Test
  public void testEvents() throws Exception {
    receiver.onEvent(new MessageProcessStep());
    
    assertTrue(receiver.getEvents().size() > 0);
    assertTrue(receiver.getEvents().size() == 0);
  }

}
