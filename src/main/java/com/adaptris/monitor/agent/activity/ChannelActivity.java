package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.adaptris.profiler.ProcessStep;
import com.google.gson.annotations.Expose;

public class ChannelActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = 1922768482203698311L;

  @Expose
  private Map<String, WorkflowActivity> workflows;

  public ChannelActivity() {
    this.setWorkflows(new LinkedHashMap<>());
  }
  
  @Override
  public void addActivity(ProcessStep processStep) {
    for(String workflowId : this.getWorkflows().keySet()) {
      this.getWorkflows().get(workflowId).addActivity(processStep);
    }
  }

  @Override
  public void resetActivity() {
    for(String workflowId : this.getWorkflows().keySet()) {
      this.getWorkflows().get(workflowId).resetActivity();
    }
  }

  public void addWorkflow(WorkflowActivity workflowActivity) {
    getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
  }

  public Map<String, WorkflowActivity> getWorkflows() {
    return workflows;
  }

  public void setWorkflows(Map<String, WorkflowActivity> workflows) {
    this.workflows = workflows;
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

  public Object clone() {
    ChannelActivity cloned = new ChannelActivity();
    getWorkflows().forEach( (id, workflow) -> {
      WorkflowActivity clonedworkflow = (WorkflowActivity) workflow.clone();
      cloned.getWorkflows().put(id, clonedworkflow);
    });
    
    cloned.setUniqueId(getUniqueId());
    
    return cloned;
  }
}
