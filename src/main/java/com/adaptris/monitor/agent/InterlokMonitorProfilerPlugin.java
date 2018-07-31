package com.adaptris.monitor.agent;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.profiler.client.ClientPlugin;
import com.adaptris.profiler.client.EventReceiver;

public class InterlokMonitorProfilerPlugin implements ClientPlugin {

  @Override
  public void close(Object object) {
    EventMonitorReceiver.getInstance().closeEventReceived((AdaptrisComponent) object);
  }

  @Override
  public void init(Object object) {
    EventMonitorReceiver.getInstance().initEventReceived((AdaptrisComponent) object);
  }

  @Override
  public void start(Object object) {
    EventMonitorReceiver.getInstance().startEventReceived((AdaptrisComponent) object);
  }

  @Override
  public void stop(Object object) {
    EventMonitorReceiver.getInstance().stopEventReceived((AdaptrisComponent) object);
  }

  @Override
  public List<EventReceiver> getReceivers() {
      final List<EventReceiver> eventReceivers = new ArrayList<EventReceiver>();
      eventReceivers.add(EventMonitorReceiver.getInstance());
      return eventReceivers;
  }
}
