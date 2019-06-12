package com.adaptris.monitor.agent;

import com.adaptris.monitor.agent.jmx.JmxEventPropagator;
import com.adaptris.monitor.agent.multicast.MulticastEventPropagator;

import junit.framework.TestCase;

public class ClientEventPropagatorCreatorTest extends TestCase {

  private static final String JMX = "JMX";
  private static final String MULTICAST = "MULTICAST";
  private static final String NONSENSE = "NONSENSE";
  
  public void setUp() throws Exception {
  }

  public void testJMXPropagator() throws Exception {
    EventPropagator createClientPropagator = ClientEventPropagatorCreator.getCreator(JMX).createClientPropagator(EventMonitorReceiver.getInstance());
    
    assertTrue(createClientPropagator instanceof JmxEventPropagator);
  }
  
  public void testMulticastPropagator() throws Exception {
    EventPropagator createClientPropagator = ClientEventPropagatorCreator.getCreator(MULTICAST).createClientPropagator(EventMonitorReceiver.getInstance());
    
    assertTrue(createClientPropagator instanceof MulticastEventPropagator);
  }
  
  public void testJMXPropagatorWithNonsenseSetting() throws Exception {
    EventPropagator createClientPropagator = ClientEventPropagatorCreator.getCreator(NONSENSE).createClientPropagator(EventMonitorReceiver.getInstance());
    
    assertTrue(createClientPropagator instanceof JmxEventPropagator);
  }
}
