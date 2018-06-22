package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChannelActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = 1922768482203698311L;

  private AdapterActivity parent;

  private Map<String, WorkflowActivity> workflows;

  public ChannelActivity() {
    workflows = new LinkedHashMap<>();
  }

  public void addWorkflow(WorkflowActivity workflowActivity) {
    getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
  }

  public AdapterActivity getParent() {
    return parent;
  }

  public void setParent(AdapterActivity parent) {
    this.parent = parent;
  }

  public Map<String, WorkflowActivity> getWorkflows() {
    return workflows;
  }

  public void setWorkflows(Map<String, WorkflowActivity> workflows) {
    this.workflows = workflows;
  }

  @Override
  public boolean equals(Object object) {
    if(object instanceof ChannelActivity) {
      if(((ChannelActivity) object).getUniqueId().equals(getUniqueId())) {
        if(((ChannelActivity) object).getParent().getUniqueId().equals(getParent().getUniqueId())) {
          return true;
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
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\tChannel = ");
    buffer.append(getUniqueId());
    buffer.append("\n");
    for(WorkflowActivity workflow : getWorkflows().values()) {
      buffer.append(workflow);
    }

    return buffer.toString();
  }
}
