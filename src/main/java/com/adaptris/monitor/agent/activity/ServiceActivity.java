package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.adaptris.profiler.ProcessStep;

public class ServiceActivity extends BaseFlowActivity implements Serializable {

  private static final long serialVersionUID = 5440965750057494954L;

  private ServiceContainerActivity parent;
  
  private Map<String, ServiceActivity> services;
  
  public ServiceActivity() {
    services = new LinkedHashMap<>();
  }
  
  @Override
  public void addActivity(ProcessStep processStep) {
    if(processStep.getStepInstanceId().equals(this.getUniqueId())) {
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
  public ServiceContainerActivity getParent() {
    return parent;
  }

  public void setParent(ServiceContainerActivity parent) {
    this.parent = parent;
  }

  @Override
  public boolean equals(Object object) {
    boolean equals = false;
    if (object instanceof ServiceActivity) {
      if (((ServiceActivity) object).getUniqueId().equals(getUniqueId())) {
        Activity parent = getParent();
        Activity objParent = ((ServiceActivity) object).getParent();
        equals = true;
        while (parent != null && objParent != null) {
          if (parent.getUniqueId().equals(objParent.getUniqueId())) {
            parent = parent.getParent();
            objParent = objParent.getParent();
          } else {
            equals = false;
          }
        }
      }
    }
    return equals;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (getUniqueId() == null ? 0 : getUniqueId().hashCode());

    Activity parent = getParent();
    while (parent != null) {
      result = prime * result + (parent.getUniqueId() == null ? 0 : parent.getUniqueId().hashCode());
      parent = parent.getParent();
    }

    return result;
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
