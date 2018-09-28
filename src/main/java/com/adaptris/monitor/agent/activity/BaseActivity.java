package com.adaptris.monitor.agent.activity;

import java.io.Serializable;

import com.adaptris.profiler.ProcessStep;

public abstract class BaseActivity implements Serializable {

  private static final long serialVersionUID = -412188093332029556L;

  private String uniqueId;
  
  public BaseActivity() {
    
  }
  
  public abstract void addActivity(ProcessStep processStep);
  
  public abstract void resetActivity();

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }
  
  public String toString() {
    return this.getUniqueId();
  }
  
}
