package com.adaptris.monitor.agent.jmx;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.adaptris.core.util.JmxHelper;
import com.adaptris.monitor.agent.AbstractEventPropagator;
import com.adaptris.monitor.agent.EventMonitorReceiver;
import com.adaptris.monitor.agent.activity.ActivityMap;

public class JmxEventPropagator extends AbstractEventPropagator {
  
  private ProfilerEventMBean eventMBean;
  
  private static ObjectName mbeanName;
  
  public JmxEventPropagator(EventMonitorReceiver eventMonitorReceiver) throws Exception {
    super(eventMonitorReceiver);
    eventMBean = new ProfilerEventMBean();
    mbeanName = new ObjectName("com.adaptris.profiler.events");
    this.registerMBean(eventMBean);
  }

  private void registerMBean(ProfilerEventMBean eventMBean2) throws MBeanRegistrationException, InstanceNotFoundException, InstanceAlreadyExistsException, NotCompliantMBeanException {
    JmxHelper.register(mbeanName, eventMBean);
  }

  @Override
  public void propagateProcessEvent(ActivityMap activityMap) {
    log.debug(activityMap.toString());

    sendJmx(activityMap);
  }

  private void sendJmx(ActivityMap activityMap) {
    this.eventMBean.addEventMap(activityMap);
  }

}
