package com.adaptris.monitor.agent.jmx;

import com.adaptris.monitor.agent.activity.ActivityMap;

public interface ProfilerEventClientMBean {

  public void addEventActivityMap(ActivityMap activityMap);
  
  public ActivityMap getEventActivityMap();
  
  public int getEventCount();
  
}
