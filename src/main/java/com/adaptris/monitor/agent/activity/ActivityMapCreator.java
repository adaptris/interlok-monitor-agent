package com.adaptris.monitor.agent.activity;

public interface ActivityMapCreator {
  
  /**
   * Accepts a representation of an Adapter, then traverses that adapter, creating an ActivityMap with correct component hierarchy.
   * @param object Some form of Adapter representation.
   * @return
   */
  public ActivityMap createBaseMap(Object object);

}
