package com.adaptris.monitor.agent.message;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.AdaptrisMessageConsumer;

public class ConsumerStructure extends ComponentStructure {

  private static final long serialVersionUID = -4986545221005856752L;

  private String destination;
  
  public ConsumerStructure() {
    
  }
  
  @Override
  public void build(AdaptrisComponent component) {
    if(component instanceof AdaptrisMessageConsumer) {
      super.build(component);
      this.setDestination(((AdaptrisMessageConsumer) component).getDestination().getDestination());
    }
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }
  
}
