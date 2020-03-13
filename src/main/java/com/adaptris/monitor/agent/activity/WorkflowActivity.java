package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.adaptris.profiler.ProcessStep;
import com.adaptris.profiler.StepType;
import com.google.gson.annotations.Expose;

public class WorkflowActivity extends BaseFlowActivity implements Serializable {

  private static final long serialVersionUID = -1630350826201321890L;

  @Expose
  private Map<String, ServiceActivity> services;

  @Expose
  private ProducerActivity producerActivity;

  @Expose
  private ConsumerActivity consumerActivity;
  
  @Expose
  private int failedCount;

  public WorkflowActivity() {
    setServices(new LinkedHashMap<>());
  }

  @Override
  public void addActivity(ProcessStep processStep) {
    if (uniqueIdAndTypeEquals(processStep, getConsumerActivity().getUniqueId(), StepType.CONSUMER)) {
      getConsumerActivity().addActivity(processStep);
    } else if (uniqueIdAndTypeEquals(processStep, getProducerActivity().getUniqueId(), StepType.PRODUCER)) {
      getProducerActivity().addActivity(processStep);
    } else if(processStep.getStepType() == StepType.SERVICE) {
      for (String serviceId : getServices().keySet()) {
        getServices().get(serviceId).addActivity(processStep);
      }
    } else { // must be a workflow activity
      if(processStep.getStepInstanceId().equals(getUniqueId())) {
        addActivityMetrics(processStep);
        if(processStep.isFailed())
          setFailedCount(getFailedCount() + 1);
      }
    }
  }

  private boolean uniqueIdAndTypeEquals(ProcessStep processStep, String uniqueId, StepType stepType) {
    String processStepID = processStep.getStepInstanceId();
    return StringUtils.equals(processStepID, uniqueId) && processStep.getStepType() == stepType;
  }

  @Override
  public void resetActivity() {
    for (String serviceId : getServices().keySet()) {
      getServices().get(serviceId).resetActivity();
    }
    getConsumerActivity().resetActivity();
    getProducerActivity().resetActivity();
    setFailedCount(0);
    super.resetActivity();
  }

  public void addServiceActivity(ServiceActivity serviceActivity) {
    getServices().put(serviceActivity.getUniqueId(), serviceActivity);
  }

  public Map<String, ServiceActivity> getServices() {
    return services;
  }

  public void setServices(Map<String, ServiceActivity> services) {
    this.services = services;
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

  public int getFailedCount() {
    return failedCount;
  }

  public void setFailedCount(int failedCount) {
    this.failedCount = failedCount;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\tWorkflow = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getMessageCount());
    buffer.append(" ( failed (");
    buffer.append(getFailedCount());
    buffer.append(" )");
    buffer.append(" at ");
    buffer.append(getAvgNsTaken());
    buffer.append("  nanos (").append(getAvgMsTaken()).append(" ms)");
    buffer.append(")");
    buffer.append("\n");
    buffer.append(getConsumerActivity());
    for (ServiceActivity service : getServices().values()) {
      buffer.append(service);
    }
    buffer.append(getProducerActivity());

    return buffer.toString();
  }

}
