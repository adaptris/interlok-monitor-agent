package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceListActivity extends ServiceActivity implements ServiceContainerActivity, Serializable {

  private static final long serialVersionUID = -3976993281634955783L;

  private Map<String, ServiceActivity> services;


  public ServiceListActivity() {
    services = new LinkedHashMap<>();
  }

  @Override
  public void addServiceActivity(ServiceActivity serviceActivity) {
    getServices().put(serviceActivity.getUniqueId(), serviceActivity);
  }

  @Override
  public Map<String, ServiceActivity> getServices() {
    return services;
  }

  @Override
  public void setServices(Map<String, ServiceActivity> services) {
    this.services = services;
  }

  @Override
  public boolean equals(Object object) {
    boolean equals = false;
    if (object instanceof ServiceListActivity) {
      if (((ServiceListActivity) object).getUniqueId().equals(getUniqueId())) {
        Activity parent = getParent();
        Activity objParent = ((ServiceListActivity) object).getParent();
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
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\t\tServiceCollection = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getMessageCount());
    buffer.append(" at ");
    buffer.append(getAvgMsTaken());
    buffer.append("  ms");
    buffer.append(")");
    buffer.append("\n");
    for (ServiceActivity service : getServices().values()) {
      buffer.append("\t").append(service);
    }

    return buffer.toString();
  }

}
