package com.adaptris.monitor.agent.message;

import java.io.Serializable;

import com.adaptris.core.AdaptrisComponent;

public class ComponentStructure implements Serializable {
  
  private static final long serialVersionUID = -543505702722319449L;

  private String componentId;
  
  private String className;
  
  public ComponentStructure() {
    
  }

  public void build(AdaptrisComponent component) {
    this.setComponentId(component.getUniqueId());
    this.setClassName(component.getClass().getName());
  }
  
  public String getComponentId() {
    return componentId;
  }

  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

}
