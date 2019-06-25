package com.adaptris.monitor.agent.jmx;

import com.adaptris.monitor.agent.EventMonitorReceiver;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;

import junit.framework.TestCase;

public class JmxEventPropagatorTest extends TestCase {
  
  private static final String ADAPTER = "adapter";
  
  private JmxEventPropagator propagator;
  
  public void setUp() throws Exception {
    propagator = new JmxEventPropagator(EventMonitorReceiver.getInstance());
  }
  
  public void tearDown() throws Exception {
    propagator.stop();
  }
  
  public void testPropagate() throws Exception {
    ActivityMap activityMap = new ActivityMap();
    activityMap.getAdapters().put(ADAPTER, new AdapterActivity());
    
    propagator.propagateProcessEvent(activityMap);
    
    assertEquals(1, propagator.getEventMBean().getEventCount());
    assertNotNull(propagator.getEventMBean().getEventActivityMap());
  }

  public void testSettingMaxHistory() throws Exception {
    propagator.getEventMBean().setMaxEventHistory(64);
    
    assertEquals(64, propagator.getEventMBean().maxEventHistory());
  }
}
