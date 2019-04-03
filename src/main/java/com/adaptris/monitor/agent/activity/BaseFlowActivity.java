package com.adaptris.monitor.agent.activity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFlowActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = -835661861179574261L;

  private String className;

  private transient List<Long> msTaken;

  @Expose
  private int messageCount;

  @Expose
  private long avgMsTaken;

  public BaseFlowActivity() {
    msTaken = new ArrayList<>();
  }
  
  protected long calculateAvgTimeTaken() {
    long totalTimeTaken = 0;
    for(long timeTaken : this.getMsTaken())
      totalTimeTaken += timeTaken;
    
    if(totalTimeTaken > 0)
      return totalTimeTaken / this.getMsTaken().size();
    else
      return 0;
  }
  
  @Override
  public void resetActivity() {
    this.setAvgMsTaken(0);
    this.setMessageCount(0);
    this.setMsTaken(new ArrayList<>());
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void addMessageId(String messageId, long timeTaken) {
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

}
