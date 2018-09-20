package com.adaptris.monitor.agent.jmx;

import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.adaptris.monitor.agent.activity.ActivityMap;

public class ProfilerEventMBean {

  private static final int DEFAULT_MAX_EVENT_HISTORY = 100;
  
  private int maxEventHistory = 0;
  
  private CircularFifoQueue<ActivityMap> eventQueue;
  
  public ProfilerEventMBean() {
    
  }

  public int getMaxEventHistory() {
    return maxEventHistory;
  }

  public void setMaxEventHistory(int maxEventHistory) {
    this.maxEventHistory = maxEventHistory;
  }
  
  public int maxEventHistory() {
    return this.getMaxEventHistory() > 0 ? this.getMaxEventHistory() : DEFAULT_MAX_EVENT_HISTORY;
  }

  public CircularFifoQueue<ActivityMap> getEventQueue() {
    if(eventQueue == null)
      this.setEventQueue((CircularFifoQueue<ActivityMap>) QueueUtils.synchronizedQueue(new CircularFifoQueue<ActivityMap>(this.maxEventHistory())));
    return this.getEventQueue();
  }

  public void setEventQueue(CircularFifoQueue<ActivityMap> eventBuffer) {
    this.eventQueue = eventBuffer;
  }
  
  public int getEventCount() {
    return this.getEventQueue().size();
  }
  
  public void addEventMap(ActivityMap activityMap) {
    this.getEventQueue().offer(activityMap);
  }
  
  public ActivityMap getEventActivityMap() {
      return (ActivityMap) this.getEventQueue().poll();
  }
  
}
