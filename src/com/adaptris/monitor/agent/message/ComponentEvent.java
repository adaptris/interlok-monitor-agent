package com.adaptris.monitor.agent.message;

import java.io.Serializable;

public class ComponentEvent implements Serializable {
  
  private static final long serialVersionUID = -7759075960281879029L;

  public static enum Event {
    START,
    
    STOP,
    
    INIT,
    
    CLOSE;
  }
  
  private Event event;
  
  private ComponentStructure component;
  
  public ComponentEvent() {
    
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public ComponentStructure getComponent() {
    return component;
  }

  public void setComponent(ComponentStructure component) {
    this.component = component;
  }

}
