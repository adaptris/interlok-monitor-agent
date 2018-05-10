package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

public class ChannelActivity extends BaseActivity implements Serializable {
  
  private static final long serialVersionUID = 1922768482203698311L;

  private AdapterActivity parent;
  
  @Expose
  private Map<String, WorkflowActivity> workflows;
  
  public ChannelActivity() {
    workflows = new LinkedHashMap<>();
  }
  
  public void addWorkflow(WorkflowActivity workflowActivity) {
    this.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
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
  
  public boolean equals(Object object) {
    if(object instanceof ChannelActivity) {
      if(((ChannelActivity) object).getUniqueId().equals(this.getUniqueId())) {
        if(((ChannelActivity) object).getParent().getUniqueId().equals(this.getParent().getUniqueId()))
          return true;
      }
    }
    return false;
  }
  
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\tChannel = ");
    buffer.append(this.getUniqueId());
    buffer.append("\n");
    for(WorkflowActivity workflow : this.getWorkflows().values()) 
      buffer.append(workflow);
    
    return buffer.toString();
  }
}
