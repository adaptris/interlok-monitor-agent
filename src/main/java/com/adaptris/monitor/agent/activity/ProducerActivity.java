package com.adaptris.monitor.agent.activity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.adaptris.profiler.ProcessStep;

public class ProducerActivity extends BaseFlowActivity implements Serializable {

  private static final long serialVersionUID = 2243255282200998908L;

  @Override
  public void addActivity(ProcessStep processStep) {
    if (StringUtils.equals(processStep.getStepInstanceId(), getUniqueId())) {
      addActivityMetrics(processStep);
    } else {
      System.err.println("Unknown producer event received");
    }
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\t\tProducer = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getMessageCount());
    buffer.append(" at ");
    buffer.append(getAvgNsTaken());
    buffer.append("  nanos (").append(getAvgMsTaken()).append(" ms)");
    buffer.append(")");
    buffer.append("\n");

    return buffer.toString();
  }

}
