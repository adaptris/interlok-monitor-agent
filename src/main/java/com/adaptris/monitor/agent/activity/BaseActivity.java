package com.adaptris.monitor.agent.activity;

import java.io.Serializable;

public abstract class BaseActivity implements Activity, Serializable {

  private static final long serialVersionUID = -412188093332029556L;

  private String uniqueId;

  public BaseActivity() {

  }

  @Override
  public String getUniqueId() {
    return uniqueId;
  }

  @Override
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  @Override
  public String toString() {
    return getUniqueId();
  }

}
