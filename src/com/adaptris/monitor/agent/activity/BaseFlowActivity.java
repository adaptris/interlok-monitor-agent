package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFlowActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = -835661861179574261L;

  private String className;

  private WorkflowActivity parent;

  private List<String> messageIds;

  private List<Long> msTaken;

  private int messageCount;

  private long avgMsTaken;

  public BaseFlowActivity() {
    messageIds = new ArrayList<>();
    msTaken = new ArrayList<>();
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
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

  public void addMessageId(String messageId, long timeTaken) {
    getMessageIds().add(messageId);
    getMsTaken().add(timeTaken);
  }

  public int getMessageCount() {
    return messageCount;
  }

  public void setMessageCount(int messageCount) {
    this.messageCount = messageCount;
  }

  public List<Long> getMsTaken() {
    return msTaken;
  }

  public void setMsTaken(List<Long> msTaken) {
    this.msTaken = msTaken;
  }

  public long getAvgMsTaken() {
    return avgMsTaken;
  }

  public void setAvgMsTaken(long avgMsTaken) {
    this.avgMsTaken = avgMsTaken;
  }

  public ChannelActivity getGrandParent() {
    return getParent() == null ? null : getParent().getParent();
  }

  public AdapterActivity getGreatGrandParent() {
    return getGrandParent() == null ? null : getGrandParent().getParent();
  }

}
