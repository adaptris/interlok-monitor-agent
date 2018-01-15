package com.adaptris.monitor.agent.message;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.AdaptrisMessageProducer;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ProduceDestination;

public class ProducerStructure extends ComponentStructure {

  private static final long serialVersionUID = 4153003960807581602L;

  private String destination;
  
  public ProducerStructure() {
    
  }

  @Override
  public void build(AdaptrisComponent component) {
    if(component instanceof AdaptrisMessageProducer) {
      super.build(component);
      try {
        ProduceDestination produceDestination = ((AdaptrisMessageProducer) component).getDestination();
        if(produceDestination != null)
          this.setDestination(produceDestination.getDestination(DefaultMessageFactory.getDefaultInstance().newMessage()));
      } catch (Exception e) {
        e.printStackTrace();
      }
      finally {
        
      }
    }
  }
  
  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }
  
}
