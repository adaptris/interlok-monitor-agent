package com.adaptris.monitor.agent;

import com.adaptris.monitor.agent.activity.ActivityMap;

public interface EventPropagator extends Runnable {
  
  public void propagateProcessEvent(ActivityMap activityMap);
  
  public void startPropagator();
  
  public void stopPropagator();

}
