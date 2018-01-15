package com.adaptris.monitor.agent;

import com.adaptris.monitor.agent.activity.ActivityMap;

public interface EventReceiverListener {

  public void eventReceived(ActivityMap activityMap);
  
}
