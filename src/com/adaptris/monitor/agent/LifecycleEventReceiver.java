package com.adaptris.monitor.agent;

import com.adaptris.core.AdaptrisComponent;

public interface LifecycleEventReceiver {
  
  public void startEventReceived(AdaptrisComponent component);
  
  public void initEventReceived(AdaptrisComponent component);
  
  public void stopEventReceived(AdaptrisComponent component);
  
  public void closeEventReceived(AdaptrisComponent component);

}
