package com.adaptris.monitor.agent.jmx;

import java.util.Queue;

import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.monitor.agent.activity.ActivityMap;

public class ProfilerEventClient implements ProfilerEventClientMBean {
  
  protected transient Logger log = LoggerFactory.getLogger(this.getClass());

  private static final int DEFAULT_MAX_EVENT_HISTORY = 100;
  
  private int maxEventHistory = 0;
  
  private Queue<ActivityMap> eventQueue;
  
  public ProfilerEventClient() {
    
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

  public Queue<ActivityMap> getEventQueue() {
    if(eventQueue == null)
      this.setEventQueue(QueueUtils.synchronizedQueue(new CircularFifoQueue<ActivityMap>(this.maxEventHistory())));
    
    log.trace("get queue: " + eventQueue.size());
    return eventQueue;
  }

  public void setEventQueue(Queue<ActivityMap> eventBuffer) {
    this.eventQueue = eventBuffer;
  }
  
  public int getEventCount() {
    log.trace("get count: " + this.getEventQueue().size());
    return this.getEventQueue().size();
  }
  
  public void addEventActivityMap(ActivityMap activityMap) {
    this.getEventQueue().offer(activityMap);
    log.trace("Add: " + this.getEventCount());
  }
  
  public ActivityMap getEventActivityMap() {
    log.trace("get map: " + this.getEventCount());
    return (ActivityMap) this.getEventQueue().poll();
  }
  
}
