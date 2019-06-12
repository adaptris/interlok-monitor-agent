package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.adaptris.profiler.ProcessStep;
import com.adaptris.profiler.StepType;
import com.google.gson.annotations.Expose;

public class WorkflowActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = -1630350826201321890L;

  @Expose
  private Map<String, ServiceActivity> services;

  @Expose
  private ProducerActivity producerActivity;

  @Expose
  private ConsumerActivity consumerActivity;

  public WorkflowActivity() {
    this.setServices(new LinkedHashMap<>());
  }
  
  @Override
  public void addActivity(ProcessStep processStep) {
    String processStepID = processStep.getStepInstanceId();
    if (StringUtils.equals(processStepID, this.getConsumerActivity().getUniqueId()) ||
            processStep.getStepType() == StepType.CONSUMER) {
      this.getConsumerActivity().addActivity(processStep);
    } else if (StringUtils.equals(processStepID, this.getProducerActivity().getUniqueId()) ||
            processStep.getStepType() == StepType.PRODUCER)
      this.getProducerActivity().addActivity(processStep);
    else {
      for (String serviceId : this.getServices().keySet()) {
        this.getServices().get(serviceId).addActivity(processStep);
      }
    }
  }

  @Override
  public void resetActivity() {
    for(String serviceId : this.getServices().keySet()) {
      this.getServices().get(serviceId).resetActivity();
    }
    this.getConsumerActivity().resetActivity();
    this.getProducerActivity().resetActivity();
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

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\tWorkflow = ");
    buffer.append(getUniqueId());
    buffer.append("\n");
    buffer.append(getConsumerActivity());
    for(ServiceActivity service : getServices().values()) {
      buffer.append(service);
    }
    buffer.append(getProducerActivity());

    return buffer.toString();
  }

}
