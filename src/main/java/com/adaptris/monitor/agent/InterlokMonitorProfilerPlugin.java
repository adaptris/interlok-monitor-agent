package com.adaptris.monitor.agent;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.Adapter;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.ActivityMapCreator;
import com.adaptris.monitor.agent.activity.AdapterInstanceActivityMapCreator;
import com.adaptris.profiler.client.ClientPlugin;
import com.adaptris.profiler.client.EventReceiver;

public class InterlokMonitorProfilerPlugin implements ClientPlugin {
  
  private ActivityMapCreator activityMapCreator;
  
  public InterlokMonitorProfilerPlugin() {
    activityMapCreator = new AdapterInstanceActivityMapCreator();
  }

  @Override
  public void close(Object object) {
  }

  @Override
  public void init(Object object) {
  }

  @Override
  public void start(Object object) {
    if(object instanceof Adapter) {
      ActivityMap createBaseMap = activityMapCreator.createBaseMap(object);
      try {
        EventMonitorReceiver.getInstance().setAdapterActivityMap(createBaseMap);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void stop(Object object) {
  }

  @Override
  public List<EventReceiver> getReceivers() {
      final List<EventReceiver> eventReceivers = new ArrayList<EventReceiver>();
      try {
        eventReceivers.add(EventMonitorReceiver.getInstance());
      } catch (Exception e) {
        e.printStackTrace();
      }
      return eventReceivers;
  }
}
