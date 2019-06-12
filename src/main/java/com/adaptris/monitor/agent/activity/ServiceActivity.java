package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.adaptris.profiler.ProcessStep;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang.StringUtils;

public class ServiceActivity extends BaseFlowActivity implements Serializable {

  private static final long serialVersionUID = 5440965750057494954L;

  @Expose
  private Map<String, ServiceActivity> services;
  
  public ServiceActivity() {
    this.setServices(new LinkedHashMap<>());
  }
  
  @Override
  public void addActivity(ProcessStep processStep) {
    if (StringUtils.equals(processStep.getStepInstanceId(), this.getUniqueId())) {
      this.getMsTaken().add(processStep.getTimeTakenMs());
      this.setAvgMsTaken(super.calculateAvgTimeTaken());
      this.setMessageCount(this.getMessageCount() + 1);
    } else {
      for(String serviceId : this.getServices().keySet()) {
        if(processStep.getStepInstanceId().equals(serviceId))
          this.getServices().get(serviceId).addActivity(processStep);
      }
    }
  }

  public Map<String, ServiceActivity> getServices() {
    return services;
  }

  public void setServices(Map<String, ServiceActivity> services) {
    this.services = services;
  }

  @Override
  public String toString() {
    return this.toString(3);
  }
  
  public String toString(int indent) {
    StringBuffer buffer = new StringBuffer();
    for(int indentIndex = 0; indentIndex < indent; indentIndex ++)
      buffer.append("\t");
    buffer.append("Service = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getMessageCount());
    buffer.append(" at ");
    buffer.append(getAvgMsTaken());
    buffer.append("  nanos (" + getAvgMsTaken() / 1000000 + " ms)");
    buffer.append(")");
    buffer.append("\n");
    
    for(ServiceActivity service : getServices().values()) {
      buffer.append(service.toString(indent + 1));
    }

    return buffer.toString();
  }

}
