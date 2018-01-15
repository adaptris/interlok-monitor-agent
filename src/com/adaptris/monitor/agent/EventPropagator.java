package com.adaptris.monitor.agent;

import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.message.ComponentEvent;

public interface EventPropagator extends Runnable {
  
  public void propagateProcessEvent(ActivityMap activityMap);
  
  public void propagateLifecycleEvent(ComponentEvent componentEvent);
  
  public void startPropagator();
  
  public void stopPropagator();

}
