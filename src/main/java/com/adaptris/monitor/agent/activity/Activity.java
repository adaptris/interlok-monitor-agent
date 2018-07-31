package com.adaptris.monitor.agent.activity;

public interface Activity {

  String getUniqueId();

  void setUniqueId(String uniqueId);

  Activity getParent();

}
