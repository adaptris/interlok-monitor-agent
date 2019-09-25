package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.adaptris.profiler.ProcessStep;
import com.google.gson.annotations.Expose;

public class ServiceActivity extends BaseFlowActivity implements Serializable {

  private static final long serialVersionUID = 5440965750057494954L;

  @Expose
  private long order = -1;

  @Expose
  private Map<String, ServiceActivity> services;

  public ServiceActivity() {
    setServices(new LinkedHashMap<>());
  }

  @Override
  public void addActivity(ProcessStep processStep) {
    if (StringUtils.equals(processStep.getStepInstanceId(), getUniqueId())) {
      addActivityMetrics(processStep);
      setOrder(processStep.getOrder());
    } else {
      for(String serviceId : getServices().keySet()) {
        if(processStep.getStepInstanceId().equals(serviceId)) {
          getServices().get(serviceId).addActivity(processStep);
        }
      }
    }
  }

  @Override
  public void resetActivity() {
    super.resetActivity();
    for(String serviceId : getServices().keySet()) {
      getServices().get(serviceId).resetActivity();
    }
  }

  public long getOrder() {
    return order;
  }

  public void setOrder(long order) {
    this.order = order;
  }

  public Map<String, ServiceActivity> getServices() {
    return services;
  }

  public void setServices(Map<String, ServiceActivity> services) {
    this.services = services;
  }

  @Override
  public String toString() {
    return toString(3);
  }

  public String toString(int indent) {
    StringBuffer buffer = new StringBuffer();
    for(int indentIndex = 0; indentIndex < indent; indentIndex ++) {
      buffer.append("\t");
    }
    buffer.append("Service = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getOrder());
    buffer.append(")");
    buffer.append(" (");
    buffer.append(getMessageCount());
    buffer.append(" at ");
    buffer.append(getAvgNsTaken());
    buffer.append("  nanos (").append(getAvgMsTaken()).append(" ms)");
    buffer.append(")");
    buffer.append("\n");

    for(ServiceActivity service : getServices().values()) {
      buffer.append(service.toString(indent + 1));
    }

    return buffer.toString();
  }

}
