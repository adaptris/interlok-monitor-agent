package com.adaptris.monitor.agent;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.profiler.ProcessStep;

public abstract class AbstractEventPropagator implements EventPropagator {
  
  protected transient Logger log = LoggerFactory.getLogger(this.getClass());
  
  private volatile boolean running;
  
  private static final int DELAY_ON_POLL_MS = 5000;

  private EventMonitorReceiver eventMonitorReciever;
  
  public AbstractEventPropagator(EventMonitorReceiver eventMonitorReceiver) throws Exception {
    eventMonitorReciever = eventMonitorReceiver;
    running = true;
  }
  
  protected abstract void stop();

  @Override
  public void run() {
    while(running) {
      try {
        Thread.sleep(DELAY_ON_POLL_MS);
      } catch (InterruptedException e) {
        running = false;
      }

      List<ProcessStep> events = eventMonitorReciever.getEvents();
      log.debug("Getting process events - " + events.size());
      if(events.size() > 0) {
        try {
          // create a map of the events we have seen, then send the map.
          ActivityMap activityMap = eventMonitorReciever.getAdapterActivityMap();
          activityMap.resetActivity();
          for(ProcessStep step : events) {
            activityMap.addActivity(step);
          }

          propagateProcessEvent(activityMap);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    }
  }
  
  @Override
  public void startPropagator() {

  }

  @Override
  public void stopPropagator() {
    running = false;
    stop();
  }
  
}
