package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

public class WorkflowActivity extends BaseActivity implements Serializable {
    
  private static final long serialVersionUID = -1630350826201321890L;

  private ChannelActivity parent;
  
  @Expose
  private Map<String, ServiceActivity> services;
  
  @Expose
  private ProducerActivity producerActivity;
  @Expose
  private ConsumerActivity consumerActivity;
  
  private List<String> messageIds;
  
  public WorkflowActivity() {
    services = new LinkedHashMap<>();
    messageIds = new ArrayList<>();
  }

  public void addServiceActivity(ServiceActivity serviceActivity) {
    this.getServices().put(serviceActivity.getUniqueId(), serviceActivity);
  }

  public ChannelActivity getParent() {
    return parent;
  }

  public void setParent(ChannelActivity parent) {
    this.parent = parent;
  }

  public Map<String, ServiceActivity> getServices() {
    return services;
  }

  public void setServices(Map<String, ServiceActivity> services) {
    this.services = services;
  }
  
  public boolean equals(Object object) {
    if(object instanceof WorkflowActivity) {
      if(((WorkflowActivity) object).getUniqueId().equals(this.getUniqueId())) {
        if(((WorkflowActivity) object).getParent().getUniqueId().equals(this.getParent().getUniqueId())) {
          if(((WorkflowActivity) object).getParent().getParent().getUniqueId().equals(this.getParent().getParent().getUniqueId()))
            return true;
        }
      }
    }
    return false;
  }
  
  public void addMessageId(String messageId) {
    if(!this.getMessageIds().contains(messageId))
      this.getMessageIds().add(messageId);
  }

  public List<String> getMessageIds() {
    return messageIds;
  }

  public void setMessageIds(List<String> messageIds) {
    this.messageIds = messageIds;
  }

  public ProducerActivity getProducerActivity() {
    return producerActivity;
  }

  public void setProducerActivity(ProducerActivity producerActivity) {
    this.producerActivity = producerActivity;
  }
  
  public ConsumerActivity getConsumerActivity() {
    return consumerActivity;
  }

  public void setConsumerActivity(ConsumerActivity consumerActivity) {
    this.consumerActivity = consumerActivity;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\tWorkflow = ");
    buffer.append(this.getUniqueId());
    buffer.append(" (");
    buffer.append(this.getMessageIds().size());
    buffer.append(")");
    buffer.append("\n");
    ConsumerActivity consumerActivity2 = this.getConsumerActivity();
    buffer.append(consumerActivity2 != null? consumerActivity2 : "");
    for(ServiceActivity service : this.getServices().values()) 
      buffer.append(service);
    ProducerActivity producerActivity2 = this.getProducerActivity();
    buffer.append(producerActivity2 != null? producerActivity2 : "");
    
    return buffer.toString();
  }

}
