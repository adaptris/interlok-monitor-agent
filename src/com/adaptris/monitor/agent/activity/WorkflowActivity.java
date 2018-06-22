package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkflowActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = -1630350826201321890L;

  private ChannelActivity parent;

  private Map<String, ServiceActivity> services;

  private ProducerActivity producerActivity;

  private ConsumerActivity consumerActivity;

  private List<String> messageIds;

  public WorkflowActivity() {
    services = new LinkedHashMap<>();
    messageIds = new ArrayList<>();
  }

  public void addServiceActivity(ServiceActivity serviceActivity) {
    getServices().put(serviceActivity.getUniqueId(), serviceActivity);
  }

  public ChannelActivity getParent() {
    return parent;
  }

  public void setParent(ChannelActivity parent) {
    this.parent = parent;
  }

  public AdapterActivity getGrandParent() {
    return getParent() == null ? null : getParent().getParent();
  }

  public Map<String, ServiceActivity> getServices() {
    return services;
  }

  public void setServices(Map<String, ServiceActivity> services) {
    this.services = services;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof WorkflowActivity) {
      if (((WorkflowActivity) object).getUniqueId().equals(getUniqueId())) {
        if (((WorkflowActivity) object).getParent().getUniqueId().equals(getParent().getUniqueId())) {
          if (((WorkflowActivity) object).getGrandParent().getUniqueId().equals(getGrandParent().getUniqueId())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (getUniqueId() == null ? 0 : getUniqueId().hashCode());
    if (getParent() != null) {
      result = prime * result + (getParent().getUniqueId() == null ? 0 : getParent().getUniqueId().hashCode());
      if (getGrandParent() != null) {
        result = prime * result + (getGrandParent().getUniqueId() == null ? 0 : getGrandParent().getUniqueId().hashCode());
      }
    }
    return result;
  }

  public void addMessageId(String messageId) {
    if(!getMessageIds().contains(messageId)) {
      getMessageIds().add(messageId);
    }
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

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\tWorkflow = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getMessageIds().size());
    buffer.append(")");
    buffer.append("\n");
    buffer.append(getConsumerActivity());
    for(ServiceActivity service : getServices().values()) {
      buffer.append(service);
    }
    buffer.append(getProducerActivity());

    return buffer.toString();
  }

}
