package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.adaptris.profiler.ProcessStep;
import com.google.gson.annotations.Expose;

public class ActivityMap implements Serializable, Cloneable {

  private static final long serialVersionUID = 2523877428476982945L;

  @Expose
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

  public Object clone() {
    ActivityMap cloned = new ActivityMap();
    
    this.getAdapters().forEach( (id, adapterActivity ) -> {
      BaseActivity clonedBaseActivity = (BaseActivity) ((AdapterActivity) adapterActivity).clone();
      cloned.getAdapters().put(id, clonedBaseActivity);
    });
    
    return cloned;
  }
}
