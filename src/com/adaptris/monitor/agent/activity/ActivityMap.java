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
          adapterComponent = interlokComponent;
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Channel)) {
          adapterComponent = interlokComponent.getParent();
        } else if (interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Workflow)) {
          adapterComponent = interlokComponent.getParent().getParent();
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Service)) {
          adapterComponent = interlokComponent.getParent().getParent().getParent();
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Producer)) {
          adapterComponent = interlokComponent.getParent().getParent().getParent();
        } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Consumer)) {
          adapterComponent = interlokComponent.getParent().getParent().getParent();
        }
      } catch (Throwable ex) {
        ex.printStackTrace();
      }
      if(adapterComponent != null) {
        AdapterActivity storedAdapterActivity = getAdapters().get(adapterComponent.getUniqueId());
        if(storedAdapterActivity == null) {
          AdapterActivity adapterActivity = new AdapterActivity();
          adapterActivity.setUniqueId(adapterComponent.getUniqueId());
          applyComponents(adapterActivity, interlokComponent, activity);
          getAdapters().put(adapterActivity.getUniqueId(), adapterActivity);
        } else {
          applyComponents(storedAdapterActivity, interlokComponent, activity);
        }
      }
    }
  }

  private void applyComponents(AdapterActivity adapterActivity, InterlokComponent interlokComponent, ProcessStep step) {
    if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Service)) {
      InterlokComponent channelComponent = interlokComponent.getParent().getParent();
      ChannelActivity channelActivity = adapterActivity.getChannels().get(channelComponent.getUniqueId());
      if(channelActivity == null) {
        channelActivity = buildChannelActivity(channelComponent, adapterActivity);
        adapterActivity.getChannels().put(channelActivity.getUniqueId(), channelActivity);
      }

      InterlokComponent workflowComponent = interlokComponent.getParent();
      WorkflowActivity workflowActivity = channelActivity.getWorkflows().get(workflowComponent.getUniqueId());
      if(workflowActivity == null) {
        workflowActivity = buildWorkflowActivity(workflowComponent, channelActivity);
        channelActivity.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
      }
      if(!workflowActivity.getMessageIds().contains(step.getMessageId())) {
        workflowActivity.addMessageId(step.getMessageId());
      }

      InterlokComponent serviceComponent = interlokComponent;
      ServiceActivity serviceActivity = workflowActivity.getServices().get(serviceComponent.getUniqueId());
      if(serviceActivity == null) {
        serviceActivity = buildServiceActivity(serviceComponent, workflowActivity);
        workflowActivity.getServices().put(serviceActivity.getUniqueId(), serviceActivity);
      }

      serviceActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
      serviceActivity.setMessageCount(serviceActivity.getMessageCount() + 1);
      serviceActivity.setServiceClass(step.getStepName());

      long totalTaken = 0;
      for(long msTaken : serviceActivity.getMsTaken()) {
        totalTaken += msTaken;
      }

      serviceActivity.setAvgMsTaken(totalTaken / serviceActivity.getMsTaken().size());

    } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Producer)) {
      InterlokComponent channelComponent = interlokComponent.getParent().getParent();
      ChannelActivity channelActivity = adapterActivity.getChannels().get(channelComponent.getUniqueId());
      if(channelActivity == null) {
        channelActivity = buildChannelActivity(channelComponent, adapterActivity);
        adapterActivity.getChannels().put(channelActivity.getUniqueId(), channelActivity);
      }

      InterlokComponent workflowComponent = interlokComponent.getParent();
      WorkflowActivity workflowActivity = channelActivity.getWorkflows().get(workflowComponent.getUniqueId());
      if(workflowActivity == null) {
        workflowActivity = buildWorkflowActivity(workflowComponent, channelActivity);
        channelActivity.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
      }
      if(!workflowActivity.getMessageIds().contains(step.getMessageId())) {
        workflowActivity.addMessageId(step.getMessageId());
      }

      ProducerActivity producerActivity = workflowActivity.getProducerActivity();
      if(producerActivity == null) {
        producerActivity = buildProducerActivity(interlokComponent, workflowActivity);
        workflowActivity.setProducerActivity(producerActivity);
      }

      producerActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());

      producerActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
      producerActivity.setMessageCount(producerActivity.getMessageCount() + 1);

      long totalTaken = 0;
      for(long msTaken : producerActivity.getMsTaken()) {
        totalTaken += msTaken;
      }

      producerActivity.setAvgMsTaken(totalTaken / producerActivity.getMsTaken().size());

    } else if(interlokComponent.getComponentType().equals(InterlokComponent.ComponentType.Consumer)) {
      InterlokComponent channelComponent = interlokComponent.getParent().getParent();
      ChannelActivity channelActivity = adapterActivity.getChannels().get(channelComponent.getUniqueId());
      if(channelActivity == null) {
        channelActivity = buildChannelActivity(channelComponent, adapterActivity);
        adapterActivity.getChannels().put(channelActivity.getUniqueId(), channelActivity);
      }

      InterlokComponent workflowComponent = interlokComponent.getParent();
      WorkflowActivity workflowActivity = channelActivity.getWorkflows().get(workflowComponent.getUniqueId());
      if(workflowActivity == null) {
        workflowActivity = buildWorkflowActivity(workflowComponent, channelActivity);
        channelActivity.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
      }
      if(!workflowActivity.getMessageIds().contains(step.getMessageId())) {
        workflowActivity.addMessageId(step.getMessageId());
      }

      ConsumerActivity consumerActivity = workflowActivity.getConsumerActivity();
      if(consumerActivity == null) {
        consumerActivity = buildConsumerActivity(interlokComponent, workflowActivity);
        workflowActivity.setConsumerActivity(consumerActivity);
      }

      consumerActivity.addMessageId(step.getMessageId());
    }
  }

  private ChannelActivity buildChannelActivity(InterlokComponent channelComponent, AdapterActivity adapterActivity) {
    ChannelActivity channelActivity = new ChannelActivity();
    channelActivity.setUniqueId(channelComponent.getUniqueId());
    channelActivity.setParent(adapterActivity);
    return channelActivity;
  }

  private WorkflowActivity buildWorkflowActivity(InterlokComponent workflowComponent, ChannelActivity channelActivity) {
    WorkflowActivity workflowActivity = new WorkflowActivity();
    workflowActivity.setUniqueId(workflowComponent.getUniqueId());
    workflowActivity.setParent(channelActivity);
    return workflowActivity;
  }

  private ConsumerActivity buildConsumerActivity(InterlokComponent consumerComponent, WorkflowActivity workflowActivity) {
    ConsumerActivity consumerActivity = new ConsumerActivity();
    consumerActivity.setUniqueId(consumerComponent.getUniqueId());
    consumerActivity.setParent(workflowActivity);
    consumerActivity.setDestination(consumerComponent.getDestination());
    consumerActivity.setConsumerClass(consumerComponent.getClassName());
    consumerActivity.setVendorImpClass(consumerComponent.getVendorImp());
    return consumerActivity;
  }

  private ProducerActivity buildProducerActivity(InterlokComponent producerComponent, WorkflowActivity workflowActivity) {
    ProducerActivity producerActivity = new ProducerActivity();
    producerActivity.setUniqueId(producerComponent.getUniqueId());
    producerActivity.setParent(workflowActivity);
    producerActivity.setDestination(producerComponent.getDestination());
    producerActivity.setConsumerClass(producerComponent.getClassName());
    producerActivity.setVendorImpClass(producerComponent.getVendorImp());
    return producerActivity;
  }

  private ServiceActivity buildServiceActivity(InterlokComponent serviceComponent, WorkflowActivity workflowActivity) {
    ServiceActivity serviceActivity = new ServiceActivity();
    serviceActivity.setUniqueId(serviceComponent.getUniqueId());
    serviceActivity.setParent(workflowActivity);
    return serviceActivity;
  }

  public Map<String, AdapterActivity> getAdapters() {
    return adapters;
  }

  public void setAdapters(Map<String, AdapterActivity> adapters) {
    this.adapters = adapters;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for(AdapterActivity adapter : getAdapters().values()) {
      buffer.append(adapter.toString());
    }
    return buffer.toString();
  }

}
