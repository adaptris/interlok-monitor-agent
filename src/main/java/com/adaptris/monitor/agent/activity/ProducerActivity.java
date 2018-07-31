package com.adaptris.monitor.agent.activity;

import java.io.Serializable;

public class ProducerActivity extends EndpointActivity implements Serializable {

  private static final long serialVersionUID = 2243255282200998908L;

  @Override
  public boolean equals(Object object) {
    if (object instanceof ProducerActivity) {
      if (((ProducerActivity) object).getUniqueId().equals(getUniqueId())) {
        if (((ProducerActivity) object).getParent().getUniqueId().equals(getParent().getUniqueId())) {
          if (((ProducerActivity) object).getGrandParent().getUniqueId().equals(getGrandParent().getUniqueId())) {
            if (((ProducerActivity) object).getGreatGrandParent().getUniqueId().equals(getGreatGrandParent().getUniqueId())) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (getUniqueId() == null ? 0 : getUniqueId().hashCode());
    if (getParent() != null) {
      result = prime * result + (getParent().getUniqueId() == null ? 0 : getParent().getUniqueId().hashCode());
      if (getGrandParent() != null) {
        result = prime * result + (getGrandParent().getUniqueId() == null ? 0 : getGrandParent().getUniqueId().hashCode());
        if (getGreatGrandParent() != null) {
          result = prime * result + (getGreatGrandParent().getUniqueId() == null ? 0 : getGreatGrandParent().getUniqueId().hashCode());
        }
      }
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\t\t\tProducer = ");
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
