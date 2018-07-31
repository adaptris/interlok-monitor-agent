package com.adaptris.monitor.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.adaptris.core.Adapter;
import com.adaptris.core.AdaptrisComponent;
import com.adaptris.monitor.agent.message.AdapterStructure;
import com.adaptris.monitor.agent.message.ComponentEvent;
import com.adaptris.profiler.ProcessStep;
import com.adaptris.profiler.client.EventReceiver;

public final class EventMonitorReceiver implements EventReceiver, LifecycleEventReceiver {

  private static EventMonitorReceiver INSTANCE;

  private EventPropagator eventPropagator;

  private final List<ProcessStep> unprocessedEvents;

  private final List<ComponentEvent> unprocessedLifecycleEvents;

  private final ReentrantLock unprocessedListLock = new ReentrantLock(false);

  private final ReentrantLock unprocessedComponentEventLock = new ReentrantLock(false);

  private EventMonitorReceiver() {
    unprocessedEvents = new ArrayList<>();
    unprocessedLifecycleEvents = new ArrayList<>();
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
      Collections.sort(returnedList, new ProcessStepComparator());
      unprocessedEvents.clear();
      return returnedList;
    } finally {
      unprocessedListLock.unlock();
    }
  }

  public List<ComponentEvent> getLifeCycleEvents() {
    try {
      unprocessedComponentEventLock.lock();
      ArrayList<ComponentEvent> returnedList = new ArrayList<ComponentEvent>(unprocessedLifecycleEvents);
      unprocessedEvents.clear();
      return returnedList;
    } finally {
      unprocessedComponentEventLock.unlock();
    }
  }

  @Override
  public void startEventReceived(AdaptrisComponent component) {
    if(component instanceof Adapter) {
      ComponentEvent lifecycleEvent = buildLifecycleEvent(component, ComponentEvent.Event.START);
      try {
        unprocessedComponentEventLock.lock();
        unprocessedLifecycleEvents.add(lifecycleEvent);
      } finally {
        unprocessedComponentEventLock.unlock();
      }
    }
  }

  @Override
  public void initEventReceived(AdaptrisComponent component) {
    if(component instanceof Adapter) {
      ComponentEvent lifecycleEvent = buildLifecycleEvent(component, ComponentEvent.Event.INIT);
      try {

        unprocessedComponentEventLock.lock();
        unprocessedLifecycleEvents.add(lifecycleEvent);
      } finally {
        unprocessedComponentEventLock.unlock();
      }
    }
  }

  @Override
  public void stopEventReceived(AdaptrisComponent component) {
    if(component instanceof Adapter) {
      ComponentEvent lifecycleEvent = buildLifecycleEvent(component, ComponentEvent.Event.STOP);
      try {
        unprocessedComponentEventLock.lock();
        unprocessedLifecycleEvents.add(lifecycleEvent);
      } finally {
        unprocessedComponentEventLock.unlock();
      }
    }
  }

  @Override
  public void closeEventReceived(AdaptrisComponent component) {
    if(component instanceof Adapter) {
      ComponentEvent lifecycleEvent = buildLifecycleEvent(component, ComponentEvent.Event.CLOSE);
      try {
        unprocessedComponentEventLock.lock();
        unprocessedLifecycleEvents.add(lifecycleEvent);
      } finally {
        unprocessedComponentEventLock.unlock();
      }
    }
  }

  private ComponentEvent buildLifecycleEvent(AdaptrisComponent component, ComponentEvent.Event lifecycleEvent) {
    if(component instanceof Adapter) {
      Adapter adapter = (Adapter) component;
      AdapterStructure adapterStructure = new AdapterStructure();
      adapterStructure.build(adapter);

      ComponentEvent componentEvent = new ComponentEvent();
      componentEvent.setEvent(lifecycleEvent);
      componentEvent.setComponent(adapterStructure);

      return componentEvent;
    } else {
      return null;
    }
  }

  private static class ProcessStepComparator implements Comparator<ProcessStep> {
    @Override
    public int compare(ProcessStep ps1, ProcessStep ps2) {
      return ps1.getOrder() < ps2.getOrder() ? -1 : ps1.getOrder() > ps2.getOrder() ? 1 : 0;
    }
  }

  public EventPropagator getEventPropagator() {
    return eventPropagator;
  }

  public void setEventPropagator(EventPropagator eventPropagator) {
    this.eventPropagator = eventPropagator;
  }

}
