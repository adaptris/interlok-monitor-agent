package com.adaptris.monitor.agent.jmx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.adaptris.monitor.agent.EventMonitorReceiver;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;

public class JmxEventPropagatorTest {
  
  private static final String ADAPTER = "adapter";
  
  private JmxEventPropagator propagator;
  
  @Before
  public void setUp() throws Exception {
    propagator = new JmxEventPropagator(EventMonitorReceiver.getInstance());
  }
  
  @After
  public void tearDown() throws Exception {
    propagator.stop();
  }
  
  @Test
  public void testPropagate() throws Exception {
    ActivityMap activityMap = new ActivityMap();
    activityMap.getAdapters().put(ADAPTER, new AdapterActivity());
    
    propagator.propagateProcessEvent(activityMap);
    
    assertEquals(1, propagator.getEventMBean().getEventCount());
    assertNotNull(propagator.getEventMBean().getEventActivityMap());
  }

  @Test
  public void testSettingMaxHistory() throws Exception {
    propagator.getEventMBean().setMaxEventHistory(64);
    
    assertEquals(64, propagator.getEventMBean().maxEventHistory());
  }
}
