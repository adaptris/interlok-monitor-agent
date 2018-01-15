package com.adaptris.monitor.agent.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.Service;
import com.adaptris.core.WorkflowImp;

public class WorkflowStructure extends ComponentStructure implements Serializable {

  private static final long serialVersionUID = -5883221674553308553L;

  private List<ServiceStructure> services;
  
  private ProducerStructure producer;
  
  private ConsumerStructure consumer;
  
  public List<ServiceStructure> getServices() {
    return services;
  }
  
  @Override
  public void build(AdaptrisComponent component) {
    if(component instanceof WorkflowImp) {
      super.build(component);
      WorkflowImp workflow = (WorkflowImp) component;
      for(Service service : workflow.getServiceCollection()) {
        ServiceStructure serviceStructure = new ServiceStructure();
        serviceStructure.build(service);
        this.getServices().add(serviceStructure);
      }
            
      this.setProducer(new ProducerStructure());
      this.getProducer().build(workflow.getProducer());
      
      this.setConsumer(new ConsumerStructure());
      this.getConsumer().build(workflow.getConsumer());
    }
  }

  public void setServices(List<ServiceStructure> services) {
    this.services = services;
  }

  public WorkflowStructure() {
    services = new ArrayList<>();
  }

  public ProducerStructure getProducer() {
    return producer;
  }

  public void setProducer(ProducerStructure producer) {
    this.producer = producer;
  }

  public ConsumerStructure getConsumer() {
    return consumer;
  }

  public void setConsumer(ConsumerStructure consumer) {
    this.consumer = consumer;
  }
  
}
