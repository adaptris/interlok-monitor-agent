package com.adaptris.monitor.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.profiler.ProcessStep;
import com.adaptris.profiler.client.EventReceiver;

public final class EventMonitorReceiver implements EventReceiver {

  private static EventMonitorReceiver INSTANCE;
  
  private EventPropagator eventPropagator;
  
  private List<ProcessStep> unprocessedEvents;
    
  private ReentrantLock unprocessedListLock = new ReentrantLock(false);

  private ActivityMap adapterActivityMap;
    
  private EventMonitorReceiver() {
    unprocessedEvents = new ArrayList<>();
    eventPropagator = new MulticastEventPropagator(this);
  }
  
  public static EventMonitorReceiver getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new EventMonitorReceiver();
      
      new Thread(new Runnable() {
        
        @Override
        public void run() {
          ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
          newSingleThreadExecutor.submit(INSTANCE.getEventPropagator());
        }
      }).start();
      
    }
    
    return INSTANCE;
  }
  
  @Override
  public void onEvent(ProcessStep processStep) {
    unprocessedListLock.lock();
    try {
      unprocessedEvents.add(processStep);
    } finally {
      unprocessedListLock.unlock();
    }
  }
  
  public List<ProcessStep> getEvents() {
    unprocessedListLock.lock();
    try {
       ArrayList<ProcessStep> returnedList = new ArrayList<ProcessStep>(unprocessedEvents);
       unprocessedEvents.clear();
       return returnedList;
    } finally {
      unprocessedListLock.unlock();
    }
  }

  public EventPropagator getEventPropagator() {
    return eventPropagator;
  }

  public void setEventPropagator(EventPropagator eventPropagator) {
    this.eventPropagator = eventPropagator;
  }

  public void setAdapterActivityMap(ActivityMap createBaseMap) {
    this.adapterActivityMap = createBaseMap;
  }
  
  public ActivityMap getAdapterActivityMap() {
    return this.adapterActivityMap;
  }
  
}
