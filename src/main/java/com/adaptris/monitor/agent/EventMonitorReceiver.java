package com.adaptris.monitor.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.profiler.ProcessStep;
import com.adaptris.profiler.ProfilerSettings;
import com.adaptris.profiler.client.EventReceiver;

public final class EventMonitorReceiver implements EventReceiver {

  private static final String EVENT_PROPAGATOR_KEY = "com.adaptris.monitor.agent.EventPropagator";

  private static EventMonitorReceiver INSTANCE;

  private EventPropagator eventPropagator;

  private final List<ProcessStep> unprocessedEvents;

  private final ReentrantLock unprocessedListLock = new ReentrantLock(false);

  private ActivityMap adapterActivityMap;

  private EventMonitorReceiver() throws Exception {
    unprocessedEvents = new ArrayList<>();
    eventPropagator = ClientEventPropagatorCreator.getCreator(ProfilerSettings.getProperty(EVENT_PROPAGATOR_KEY)).createClientPropagator(this);
  }

  public static EventMonitorReceiver getInstance() throws Exception {
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
      ArrayList<ProcessStep> returnedList = new ArrayList<>(unprocessedEvents);
      Collections.sort(returnedList, new ProcessStepComparator());
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
    adapterActivityMap = createBaseMap;
  }

  public ActivityMap getAdapterActivityMap() {
    return adapterActivityMap;
  }

  private static class ProcessStepComparator implements Comparator<ProcessStep> {
    @Override
    public int compare(ProcessStep ps1, ProcessStep ps2) {
      return ps1.getOrder() < ps2.getOrder() ? -1 : ps1.getOrder() > ps2.getOrder() ? 1 : 0;
    }
  }

}
