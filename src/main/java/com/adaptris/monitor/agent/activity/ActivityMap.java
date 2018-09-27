package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.adaptris.profiler.ProcessStep;

public class ActivityMap implements Serializable {

  private static final long serialVersionUID = 2523877428476982945L;

  private Map<String, BaseActivity> adapters;

  public ActivityMap() {
    adapters = new HashMap<>();
  }

  public void addActivity(ProcessStep activity) {
    for(String key : this.getAdapters().keySet()) {
      this.getAdapters().get(key).addActivity(activity);
    }
  }
  
  public void resetActivity() {
    for(String key : this.getAdapters().keySet()) {
      this.getAdapters().get(key).resetActivity();
    }
  }

  public Map<String, BaseActivity> getAdapters() {
    return adapters;
  }

  public void setAdapters(Map<String, BaseActivity> adapters) {
    this.adapters = adapters;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for(BaseActivity adapter : getAdapters().values()) {
      buffer.append(adapter.toString());
    }
    return buffer.toString();
  }

}
