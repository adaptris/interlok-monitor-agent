package com.adaptris.monitor.agent.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.Channel;
import com.adaptris.core.Workflow;

public class ChannelStructure extends ComponentStructure implements Serializable {

  private static final long serialVersionUID = 898650289218996116L;

  private List<WorkflowStructure> workflows;
  
  public ChannelStructure() {
    workflows = new ArrayList<>();
  }
  
  @Override
  public void build(AdaptrisComponent component) {
    if(component instanceof Channel) {
      super.build(component);

      Channel channel = (Channel) component;
      for(Workflow workflow : channel.getWorkflowList()) {
        WorkflowStructure workflowStructure = new WorkflowStructure();
        workflowStructure.build(workflow);
        this.getWorkflows().add(workflowStructure);
      }
    }
  }

  public List<WorkflowStructure> getWorkflows() {
    return workflows;
  }

  public void setWorkflows(List<WorkflowStructure> workflows) {
    this.workflows = workflows;
  }
  
}
