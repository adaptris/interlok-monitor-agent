package com.adaptris.monitor.agent.jmx;

import java.util.List;

import com.adaptris.core.CoreException;
import com.adaptris.monitor.agent.activity.ActivityMap;

public interface ProfilerEventClientMBean {

  public void addEventActivityMap(ActivityMap activityMap) throws CoreException;
  
  public List<ActivityMap> getEventActivityMaps() throws CoreException;
  
  public int getEventCount() throws CoreException;
  
}
