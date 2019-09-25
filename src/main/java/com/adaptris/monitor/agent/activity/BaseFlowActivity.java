package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.adaptris.profiler.ProcessStep;
import com.google.gson.annotations.Expose;

public abstract class BaseFlowActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = -835661861179574261L;

  private String className;

  private transient List<Long> nsTaken;

  @Expose
  private int messageCount;

  @Expose
  private long avgNsTaken;

  @Expose
  private long avgMsTaken;

  public BaseFlowActivity() {
    nsTaken = new ArrayList<>();
  }

  protected long calculateAvgTimeTaken() {
    long totalTimeTaken = 0;
    for (long timeTaken : getNsTaken()) {
      totalTimeTaken += timeTaken;
    }
    if (totalTimeTaken > 0) {
      return totalTimeTaken / getNsTaken().size();
    } else {
      return 0;
    }
  }

  protected void addActivityMetrics(ProcessStep processStep) {
    getNsTaken().add(processStep.getTimeTakenNanos());
    setAvgNsTaken(calculateAvgTimeTaken());
    setMessageCount(getMessageCount() + 1);
  }

  @Override
  public void resetActivity() {
    setAvgNsTaken(0);
    setMessageCount(0);
    setNsTaken(new ArrayList<>());
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public int getMessageCount() {
    return messageCount;
  }

  public void setMessageCount(int messageCount) {
    this.messageCount = messageCount;
  }

  public List<Long> getNsTaken() {
    return nsTaken;
  }

  public void setNsTaken(List<Long> nsTaken) {
    this.nsTaken = nsTaken;
  }

  public long getAvgNsTaken() {
    return avgNsTaken;
  }

  public void setAvgNsTaken(long avgNsTaken) {
    this.avgNsTaken = avgNsTaken;
    avgMsTaken = avgNsTaken / 1000000;
  }

  public long getAvgMsTaken() {
    return avgMsTaken;
  }

}
