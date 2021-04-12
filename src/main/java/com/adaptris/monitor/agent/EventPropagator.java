package com.adaptris.monitor.agent;

import com.adaptris.core.CoreException;
import com.adaptris.monitor.agent.activity.ActivityMap;

public interface EventPropagator extends Runnable {
  
  public void propagateProcessEvent(ActivityMap activityMap) throws CoreException;
  
  public void startPropagator();
  
  public void stopPropagator();

}
