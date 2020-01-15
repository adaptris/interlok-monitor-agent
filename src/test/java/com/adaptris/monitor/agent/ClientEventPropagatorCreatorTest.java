package com.adaptris.monitor.agent;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.adaptris.monitor.agent.jmx.JmxEventPropagator;
import com.adaptris.monitor.agent.multicast.MulticastEventPropagator;

public class ClientEventPropagatorCreatorTest {

  private static final String JMX = "JMX";
  private static final String MULTICAST = "MULTICAST";
  private static final String NONSENSE = "NONSENSE";
  
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testJMXPropagator() throws Exception {
    EventPropagator createClientPropagator = ClientEventPropagatorCreator.getCreator(JMX).createClientPropagator(EventMonitorReceiver.getInstance());
    
    assertTrue(createClientPropagator instanceof JmxEventPropagator);
  }
  
  @Test
  public void testMulticastPropagator() throws Exception {
    EventPropagator createClientPropagator = ClientEventPropagatorCreator.getCreator(MULTICAST).createClientPropagator(EventMonitorReceiver.getInstance());
    
    assertTrue(createClientPropagator instanceof MulticastEventPropagator);
  }
  
  @Test
  public void testJMXPropagatorWithNonsenseSetting() throws Exception {
    EventPropagator createClientPropagator = ClientEventPropagatorCreator.getCreator(NONSENSE).createClientPropagator(EventMonitorReceiver.getInstance());
    
    assertTrue(createClientPropagator instanceof JmxEventPropagator);
  }
}
