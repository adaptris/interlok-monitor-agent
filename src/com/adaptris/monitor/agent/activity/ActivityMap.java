package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.adaptris.profiler.ProcessStep;
import com.adaptris.profiler.aspects.InterlokComponent;

public class ActivityMap implements Serializable {
  
  private static final long serialVersionUID = 2523877428476982945L;
  
  private Map<String, AdapterActivity> adapters;
  
  public ActivityMap() {
    adapters = new HashMap<>();
  }
  
  public void addActivity(ProcessStep activity) {
    InterlokComponent interlokComponent = activity.getInterlokComponent();
    
    if(!interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.ServiceList)) {
      InterlokComponent adapterComponent = null;
            
      try {
        if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Adapter)) {
          adapterComponent = activity.getInterlokComponent();
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Channel)) {
          adapterComponent = activity.getInterlokComponent().getParent();
        } else if (interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Workflow)) {
          adapterComponent = activity.getInterlokComponent().getParent().getParent();
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Service)) {
          adapterComponent = activity.getInterlokComponent().getParent().getParent().getParent();
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Producer)) {
          adapterComponent = activity.getInterlokComponent().getParent().getParent().getParent();
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Consumer)) {
          adapterComponent = activity.getInterlokComponent().getParent().getParent().getParent();
        }
      } catch (Throwable ex) {
        ex.printStackTrace();
      }
      if(adapterComponent != null) {
        AdapterActivity storedAdapterActivity = this.getAdapters().get(adapterComponent.getUniqueId());
        if(storedAdapterActivity == null) {
          AdapterActivity adapterActivity = new AdapterActivity();
          adapterActivity.setUniqueId(adapterComponent.getUniqueId());
          applyComponents(adapterActivity, activity.getInterlokComponent(), activity);
          this.getAdapters().put(adapterActivity.getUniqueId(), adapterActivity);
        } else
          applyComponents(storedAdapterActivity, activity.getInterlokComponent(), activity);
      }
    }
  }

  private void applyComponents(AdapterActivity adapterActivity, InterlokComponent adapterComponent, ProcessStep step) {
    if(adapterComponent.getComponentType().equals(InterlokComponent.ComponentType.Service)) {
      InterlokComponent channelComponent = adapterComponent.getParent().getParent();
      ChannelActivity channelActivity = adapterActivity.getChannels().get(channelComponent.getUniqueId());
      if(channelActivity == null) {
        channelActivity = new ChannelActivity();
        channelActivity.setUniqueId(channelComponent.getUniqueId());
        channelActivity.setParent(adapterActivity);
        adapterActivity.getChannels().put(channelActivity.getUniqueId(), channelActivity);
      }
      
      InterlokComponent workflowComponent = adapterComponent.getParent();
      WorkflowActivity workflowActivity = channelActivity.getWorkflows().get(workflowComponent.getUniqueId());
      if(workflowActivity == null) {
        workflowActivity = new WorkflowActivity();
        workflowActivity.setUniqueId(workflowComponent.getUniqueId());
        workflowActivity.setParent(channelActivity);
        channelActivity.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
      }
      if(!workflowActivity.getMessageIds().contains(step.getMessage()))
        workflowActivity.addMessageId(step.getMessageId());
      
      InterlokComponent serviceComponent = adapterComponent;
      ServiceActivity serviceActivity = workflowActivity.getServices().get(serviceComponent.getUniqueId());
      if(serviceActivity == null) {
        serviceActivity = new ServiceActivity();
        serviceActivity.setUniqueId(serviceComponent.getUniqueId());
        serviceActivity.setParent(workflowActivity);
        workflowActivity.getServices().put(serviceActivity.getUniqueId(), serviceActivity);
      }
      
      serviceActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
      serviceActivity.setMessageCount(serviceActivity.getMessageCount() + 1);
      serviceActivity.setServiceClass(step.getStepName());
      
      long totalTaken = 0;
      for(long msTaken : serviceActivity.getMsTaken())
        totalTaken += msTaken;
      
      serviceActivity.setAvgMsTaken(totalTaken / serviceActivity.getMsTaken().size());
      
    } else if(adapterComponent.getComponentType().equals(InterlokComponent.ComponentType.Producer)) {
      InterlokComponent channelComponent = adapterComponent.getParent().getParent();
      ChannelActivity channelActivity = adapterActivity.getChannels().get(channelComponent.getUniqueId());
      if(channelActivity == null) {
        channelActivity = new ChannelActivity();
        channelActivity.setUniqueId(channelComponent.getUniqueId());
        channelActivity.setParent(adapterActivity);
        adapterActivity.getChannels().put(channelActivity.getUniqueId(), channelActivity);
      }
      
      InterlokComponent workflowComponent = adapterComponent.getParent();
      WorkflowActivity workflowActivity = channelActivity.getWorkflows().get(workflowComponent.getUniqueId());
      if(workflowActivity == null) {
        workflowActivity = new WorkflowActivity();
        workflowActivity.setUniqueId(workflowComponent.getUniqueId());
        workflowActivity.setParent(channelActivity);
        channelActivity.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
      }
      if(!workflowActivity.getMessageIds().contains(step.getMessage()))
        workflowActivity.addMessageId(step.getMessageId());
      
      ProducerActivity producerActivity = workflowActivity.getProducerActivity();
      if(producerActivity == null) {
        producerActivity = new ProducerActivity();
        producerActivity.setUniqueId(adapterComponent.getUniqueId());
        producerActivity.setParent(workflowActivity);
        producerActivity.setDestination(adapterComponent.getDestination());
        producerActivity.setConsumerClass(adapterComponent.getClassName());
        producerActivity.setVendorImpClass(adapterComponent.getVendorImp());
        workflowActivity.setProducerActivity(producerActivity);
      }
      
      producerActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
      
      producerActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
      producerActivity.setMessageCount(producerActivity.getMessageCount() + 1);
      
      long totalTaken = 0;
      for(long msTaken : producerActivity.getMsTaken())
        totalTaken += msTaken;
      
      producerActivity.setAvgMsTaken(totalTaken / producerActivity.getMsTaken().size());
      
    } else if(adapterComponent.getComponentType().equals(InterlokComponent.ComponentType.Consumer)) {
      
      InterlokComponent channelComponent = adapterComponent.getParent().getParent();
      ChannelActivity channelActivity = adapterActivity.getChannels().get(channelComponent.getUniqueId());
      if(channelActivity == null) {
        channelActivity = new ChannelActivity();
        channelActivity.setUniqueId(channelComponent.getUniqueId());
        channelActivity.setParent(adapterActivity);
        adapterActivity.getChannels().put(channelActivity.getUniqueId(), channelActivity);
      }
      
      InterlokComponent workflowComponent = adapterComponent.getParent();
      WorkflowActivity workflowActivity = channelActivity.getWorkflows().get(workflowComponent.getUniqueId());
      if(workflowActivity == null) {
        workflowActivity = new WorkflowActivity();
        workflowActivity.setUniqueId(workflowComponent.getUniqueId());
        workflowActivity.setParent(channelActivity);
        channelActivity.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
      }
      if(!workflowActivity.getMessageIds().contains(step.getMessage()))
        workflowActivity.addMessageId(step.getMessageId());
      
      ConsumerActivity consumerActivity = workflowActivity.getConsumerActivity();
      if(consumerActivity == null) {
        consumerActivity = new ConsumerActivity();
        consumerActivity.setUniqueId(adapterComponent.getUniqueId());
        consumerActivity.setParent(workflowActivity);
        consumerActivity.setDestination(adapterComponent.getDestination());
        consumerActivity.setConsumerClass(adapterComponent.getClassName());
        consumerActivity.setVendorImpClass(adapterComponent.getVendorImp());
        workflowActivity.setConsumerActivity(consumerActivity);
      }
      
      consumerActivity.addMessageId(step.getMessageId());
    }
  }

  public Map<String, AdapterActivity> getAdapters() {
    return adapters;
  }

  public void setAdapters(Map<String, AdapterActivity> adapters) {
    this.adapters = adapters;
  }
  
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for(AdapterActivity adapter : this.getAdapters().values()) {
      buffer.append(adapter.toString());
    }
    return buffer.toString();
  }

}
