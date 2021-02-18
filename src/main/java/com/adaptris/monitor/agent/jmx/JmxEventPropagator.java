package com.adaptris.monitor.agent.jmx;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.adaptris.core.CoreException;
import com.adaptris.core.runtime.AdapterComponentMBean;
import com.adaptris.core.util.JmxHelper;
import com.adaptris.monitor.agent.AbstractEventPropagator;
import com.adaptris.monitor.agent.EventMonitorReceiver;
import com.adaptris.monitor.agent.activity.ActivityMap;

public class JmxEventPropagator extends AbstractEventPropagator {
  
  private ProfilerEventClient eventMBean;
  
  private static ObjectName mbeanName;
  
  public JmxEventPropagator(EventMonitorReceiver eventMonitorReceiver) throws Exception {
    super(eventMonitorReceiver);
    this.setEventMBean(new ProfilerEventClient());
    mbeanName = new ObjectName(AdapterComponentMBean.JMX_DOMAIN_NAME + ":type=Profiler");
    this.registerMBean(eventMBean);
  }

  private void registerMBean(ProfilerEventClient eventMBean2) throws MBeanRegistrationException, InstanceNotFoundException, InstanceAlreadyExistsException, NotCompliantMBeanException {
    JmxHelper.register(mbeanName, eventMBean);
  }

  @Override
  public void propagateProcessEvent(ActivityMap activityMap) throws CoreException {
    log.debug(activityMap.toString());

    sendJmx(activityMap);
  }

  private void sendJmx(ActivityMap activityMap) throws CoreException {
    this.eventMBean.addEventActivityMap(activityMap);
  }

  @Override
  protected void stop() {
  }

  public ProfilerEventClient getEventMBean() {
    return eventMBean;
  }

  public void setEventMBean(ProfilerEventClient eventMBean) {
    this.eventMBean = eventMBean;
  }

}
