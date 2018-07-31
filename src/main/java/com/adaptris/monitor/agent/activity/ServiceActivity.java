package com.adaptris.monitor.agent.activity;

import java.io.Serializable;

public class ServiceActivity extends BaseFlowActivity implements Serializable {

  private static final long serialVersionUID = 5440965750057494954L;

  private ServiceContainerActivity parent;

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
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\t\tService = ");
    buffer.append(getUniqueId());
    buffer.append(" (");
    buffer.append(getMessageCount());
    buffer.append(" at ");
    buffer.append(getAvgMsTaken());
    buffer.append("  ms");
    buffer.append(")");
    buffer.append("\n");

    return buffer.toString();
  }

}
