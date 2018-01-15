package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProducerActivity extends EndpointActivity implements Serializable {
    
  private static final long serialVersionUID = 2243255282200998908L;

  private WorkflowActivity parent;
  
  private List<String> messageIds;
  
  private List<Long> msTaken;
  
  private int messageCount;
  
  private long avgMsTaken;
  
  public ProducerActivity() {
    messageIds = new ArrayList<String>();
    msTaken = new ArrayList<>();
  }
  
  public void addMessageId(String messageId, long timeTaken) {
    this.getMessageIds().add(messageId);
    this.getMsTaken().add(timeTaken);
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
    if(object instanceof ProducerActivity) {
      if(((ProducerActivity) object).getUniqueId().equals(this.getUniqueId())) {
        if(((ProducerActivity) object).getParent().getUniqueId().equals(this.getParent().getUniqueId())) {
          if(((ProducerActivity) object).getParent().getParent().getUniqueId().equals(this.getParent().getParent().getUniqueId())) {
            if(((ProducerActivity) object).getParent().getParent().getParent().getUniqueId().equals(this.getParent().getParent().getParent().getUniqueId())) 
              return true;
          }
        }
      }
    }
    return false;
  }
  
  public List<Long> getMsTaken() {
    return msTaken;
  }

  public void setMsTaken(List<Long> msTaken) {
    this.msTaken = msTaken;
  }

  public int getMessageCount() {
    return messageCount;
  }

  public void setMessageCount(int messageCount) {
    this.messageCount = messageCount;
  }

  public long getAvgMsTaken() {
    return avgMsTaken;
  }

  public void setAvgMsTaken(long avgMsTaken) {
    this.avgMsTaken = avgMsTaken;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\t\tProducer = ");
    buffer.append(this.getUniqueId());
    buffer.append(" (");
    buffer.append(this.getMessageIds().size());
    buffer.append(" at ");
    buffer.append(this.getAvgMsTaken());
    buffer.append("  ms");
    buffer.append(")");
    buffer.append("\n");
    
    return buffer.toString();
  }

}
