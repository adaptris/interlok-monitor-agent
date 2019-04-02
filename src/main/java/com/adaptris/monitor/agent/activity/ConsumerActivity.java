package com.adaptris.monitor.agent.activity;

import java.io.Serializable;

import com.adaptris.profiler.ProcessStep;
import org.apache.commons.lang.StringUtils;

public class ConsumerActivity extends BaseFlowActivity implements Serializable {

  private static final long serialVersionUID = 2243255282200998908L;

  @Override
  public void addActivity(ProcessStep processStep) {
    if (StringUtils.equals(processStep.getStepInstanceId(), this.getUniqueId())) {
      this.getMsTaken().add(processStep.getTimeTakenMs());
      this.setAvgMsTaken(super.calculateAvgTimeTaken());
      this.setMessageCount(this.getMessageCount() + 1);
    }
    else {
      System.err.println("Unknown consumer event received");
    }
  }
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof ConsumerActivity) {
      if(((ConsumerActivity) object).getUniqueId().equals(this.getUniqueId()))
        return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (getUniqueId() == null ? 0 : getUniqueId().hashCode());
    
    return result;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\t\tConsumer = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getMessageCount());
    buffer.append(")");
    buffer.append("\n");

    return buffer.toString();
  }

}
