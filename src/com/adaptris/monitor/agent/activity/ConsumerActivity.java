package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsumerActivity extends EndpointActivity implements Serializable {
    
  private static final long serialVersionUID = 2243255282200998908L;

  private WorkflowActivity parent;
  
  private List<String> messageIds;
  
  public ConsumerActivity() {
    messageIds = new ArrayList<String>();
  }
  
  public void addMessageId(String messageId) {
    this.getMessageIds().add(messageId);
  }

  public WorkflowActivity getParent() {
    return parent;
  }

  public void setParent(WorkflowActivity parent) {
    this.parent = parent;
  }

  public List<String> getMessageIds() {
    return messageIds;
  }

  public void setMessageIds(List<String> messageIds) {
    this.messageIds = messageIds;
  }
  
  public boolean equals(Object object) {
    if(object instanceof ConsumerActivity) {
      if(((ConsumerActivity) object).getUniqueId().equals(this.getUniqueId())) {
        if(((ConsumerActivity) object).getParent().getUniqueId().equals(this.getParent().getUniqueId())) {
          if(((ConsumerActivity) object).getParent().getParent().getUniqueId().equals(this.getParent().getParent().getUniqueId())) {
            if(((ConsumerActivity) object).getParent().getParent().getParent().getUniqueId().equals(this.getParent().getParent().getParent().getUniqueId())) 
              return true;
          }
        }
      }
    }
    return false;
  }
  
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\t\tConsumer = ");
    buffer.append(this.getUniqueId());
    buffer.append(" (");
    buffer.append(this.getMessageIds().size());
    buffer.append(")");
    buffer.append("\n");
    
    return buffer.toString();
  }

}
