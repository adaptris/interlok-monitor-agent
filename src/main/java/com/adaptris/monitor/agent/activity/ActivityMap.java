package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.profiler.ProcessStep;
//import com.adaptris.profiler.aspects.InterlokComponent;
//import com.adaptris.profiler.aspects.InterlokComponent.ComponentType;

public class ActivityMap implements Serializable {

  private static final long serialVersionUID = 2523877428476982945L;

//  private transient Logger log = LoggerFactory.getLogger(this.getClass());

  private Map<String, BaseActivity> adapters;

  public ActivityMap() {
    adapters = new HashMap<>();
  }

  public void addActivity(ProcessStep activity) {
    for(String key : this.getAdapters().keySet()) {
      this.getAdapters().get(key).addActivity(activity);
    }
    
//    InterlokComponent interlokComponent = activity.getInterlokComponent();
//    InterlokComponent adapterComponent = null;
//
//    try {
//      if (interlokComponent.getComponentType().equals(ComponentType.Adapter)) {
//        adapterComponent = interlokComponent;
//      } else {
//        adapterComponent = findAdapterParent(interlokComponent);
//      }
//    } catch (Throwable ex) {
//      ex.printStackTrace();
//    }
//    if (adapterComponent != null) {
//      BaseActivity storedAdapterActivity = getAdapters().get(adapterComponent.getUniqueId());
//      if (storedAdapterActivity == null) {
//        AdapterActivity adapterActivity = new AdapterActivity();
//        adapterActivity.setUniqueId(adapterComponent.getUniqueId());
//        applyComponents(adapterActivity, interlokComponent, activity);
//        getAdapters().put(adapterActivity.getUniqueId(), adapterActivity);
//      } else if (storedAdapterActivity instanceof AdapterActivity) {
//        applyComponents((AdapterActivity) storedAdapterActivity, interlokComponent, activity);
//      }
//    } else {
//      log.debug("AdapterComponent parent is null for {}", interlokComponent.getUniqueId());
//    }
  }

//  private void applyComponents(AdapterActivity adapterActivity, InterlokComponent interlokComponent, ProcessStep step) {
//    if (interlokComponent.getComponentType().equals(ComponentType.Service)
//        || interlokComponent.getComponentType().equals(ComponentType.ServiceList)) {
//
//      ChannelActivity channelActivity = getOrBuildChannelActivity(adapterActivity, interlokComponent);
//      WorkflowActivity workflowActivity = getOrBuildWorkflowActivity(channelActivity, interlokComponent, step);
//
//      ServiceContainerActivity parentActivity = null;
//
//      InterlokComponent parentComponent = interlokComponent.getParent();
//      if (parentComponent.getComponentType().equals(ComponentType.ServiceList)) {
//        parentActivity = getOrBuildParentServiceActivity(workflowActivity, interlokComponent, step);
//      } else {
//        parentActivity = workflowActivity;
//      }
//
//      if (parentActivity == null) {
//        // XXX Can we have a null parentActivity? What about shared services?
//        log.trace("{} has a null parent", interlokComponent.getUniqueId());
//      } else {
//        ServiceActivity serviceActivity = parentActivity.getServices().get(interlokComponent.getUniqueId());
//        if (serviceActivity == null) {
//          if (interlokComponent.getComponentType().equals(ComponentType.ServiceList)) {
//            serviceActivity = buildServiceListActivity(interlokComponent, parentActivity);
//          } else {
//            serviceActivity = buildServiceActivity(interlokComponent, parentActivity);
//          }
//          parentActivity.getServices().put(serviceActivity.getUniqueId(), serviceActivity);
//        }
//
//        serviceActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
//        serviceActivity.setMessageCount(serviceActivity.getMessageCount() + 1);
//        // serviceActivity.setClassName(step.getStepName());
//
//        long avgMsTaken = calculateAvgMsTaken(serviceActivity.getMsTaken());
//        serviceActivity.setAvgMsTaken(avgMsTaken);
//      }
//
//    } else if (interlokComponent.getComponentType().equals(ComponentType.Producer)) {
//      ChannelActivity channelActivity = getOrBuildChannelActivity(adapterActivity, interlokComponent);
//      WorkflowActivity workflowActivity = getOrBuildWorkflowActivity(channelActivity, interlokComponent, step);
//
//      ProducerActivity producerActivity = workflowActivity.getProducerActivity();
//      if (producerActivity == null) {
//        producerActivity = buildProducerActivity(interlokComponent, workflowActivity);
//        workflowActivity.setProducerActivity(producerActivity);
//      }
//
//      producerActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
//      producerActivity.setMessageCount(producerActivity.getMessageCount() + 1);
//
//      long avgMsTaken = calculateAvgMsTaken(producerActivity.getMsTaken());
//      producerActivity.setAvgMsTaken(avgMsTaken);
//
//    } else if (interlokComponent.getComponentType().equals(ComponentType.Consumer)) {
//      ChannelActivity channelActivity = getOrBuildChannelActivity(adapterActivity, interlokComponent);
//      WorkflowActivity workflowActivity = getOrBuildWorkflowActivity(channelActivity, interlokComponent, step);
//
//      ConsumerActivity consumerActivity = workflowActivity.getConsumerActivity();
//      if (consumerActivity == null) {
//        consumerActivity = buildConsumerActivity(interlokComponent, workflowActivity);
//        workflowActivity.setConsumerActivity(consumerActivity);
//      }
//
//      consumerActivity.addMessageId(step.getMessageId(), step.getTimeTakenMs());
//      consumerActivity.setMessageCount(consumerActivity.getMessageCount() + 1);
//
//      long avgMsTaken = calculateAvgMsTaken(consumerActivity.getMsTaken());
//      consumerActivity.setAvgMsTaken(avgMsTaken);
//
//    }
//  }
//
//  private ChannelActivity getOrBuildChannelActivity(AdapterActivity adapterActivity, InterlokComponent interlokComponent) {
//    InterlokComponent channelComponent = findChannelParent(interlokComponent);
//    if (channelComponent != null) {
//      ChannelActivity channelActivity = adapterActivity.getChannels().get(channelComponent.getUniqueId());
//      if (channelActivity == null) {
//        channelActivity = buildChannelActivity(channelComponent, adapterActivity);
//        adapterActivity.getChannels().put(channelActivity.getUniqueId(), channelActivity);
//      }
//      return channelActivity;
//    }
//    return null;
//  }
//
//  private WorkflowActivity getOrBuildWorkflowActivity(ChannelActivity channelActivity, InterlokComponent interlokComponent, ProcessStep step) {
//    InterlokComponent workflowComponent = findWorkflowParent(interlokComponent);
//    if (workflowComponent != null) {
//      WorkflowActivity workflowActivity = channelActivity.getWorkflows().get(workflowComponent.getUniqueId());
//      if (workflowActivity == null) {
//        workflowActivity = buildWorkflowActivity(workflowComponent, channelActivity);
//        channelActivity.getWorkflows().put(workflowActivity.getUniqueId(), workflowActivity);
//      }
//      if (!workflowActivity.getMessageIds().contains(step.getMessageId())) {
//        workflowActivity.addMessageId(step.getMessageId());
//      }
//      return workflowActivity;
//    }
//    return null;
//  }
//
//  private ServiceContainerActivity getOrBuildParentServiceActivity(WorkflowActivity workflowActivity, InterlokComponent interlokComponent,
//      ProcessStep step) {
//    InterlokComponent parent = interlokComponent.getParent();
//    List<InterlokComponent> parents = new ArrayList<>();
//    while (parent != null && parent.getComponentType().equals(ComponentType.ServiceList)) {
//      parents.add(parent);
//      parent = parent.getParent();
//    }
//
//    Collections.reverse(parents);
//
//    ServiceContainerActivity parentActivity = null;
//    for (InterlokComponent parentComponent : parents) {
//      ServiceActivity serviceActivity = null;
//
//      if (parentComponent.getParent().getComponentType().equals(ComponentType.Workflow)) {
//        // Top of the list we attach it to the workflow
//        serviceActivity = workflowActivity.getServices().get(parentComponent.getUniqueId());
//        if (serviceActivity == null) {
//          serviceActivity = buildServiceListActivity(parentComponent, workflowActivity);
//          workflowActivity.getServices().put(serviceActivity.getUniqueId(), serviceActivity);
//        }
//      } else if (parentActivity != null) {
//        serviceActivity = parentActivity.getServices().get(parentComponent.getUniqueId());
//        if (serviceActivity == null) {
//          serviceActivity = buildServiceListActivity(parentComponent, parentActivity);
//          parentActivity.getServices().put(serviceActivity.getUniqueId(), serviceActivity);
//        }
//      }
//
//      if (serviceActivity instanceof ServiceContainerActivity) {
//        parentActivity = (ServiceListActivity) serviceActivity;
//      }
//    }
//
//    return parentActivity;
//  }

//  private ChannelActivity buildChannelActivity(InterlokComponent channelComponent, AdapterActivity adapterActivity) {
//    ChannelActivity channelActivity = new ChannelActivity();
//    channelActivity.setUniqueId(channelComponent.getUniqueId());
//    channelActivity.setParent(adapterActivity);
//    return channelActivity;
//  }
//
//  private WorkflowActivity buildWorkflowActivity(InterlokComponent workflowComponent, ChannelActivity channelActivity) {
//    WorkflowActivity workflowActivity = new WorkflowActivity();
//    workflowActivity.setUniqueId(workflowComponent.getUniqueId());
//    workflowActivity.setParent(channelActivity);
//    return workflowActivity;
//  }
//
//  private ConsumerActivity buildConsumerActivity(InterlokComponent consumerComponent, WorkflowActivity workflowActivity) {
//    ConsumerActivity consumerActivity = new ConsumerActivity();
//    consumerActivity.setUniqueId(consumerComponent.getUniqueId());
//    consumerActivity.setParent(workflowActivity);
//    consumerActivity.setClassName(consumerComponent.getClassName());
//    consumerActivity.setDestination(consumerComponent.getDestination());
//    consumerActivity.setVendorImpClass(consumerComponent.getVendorImp());
//    return consumerActivity;
//  }
//
//  private ProducerActivity buildProducerActivity(InterlokComponent producerComponent, WorkflowActivity workflowActivity) {
//    ProducerActivity producerActivity = new ProducerActivity();
//    producerActivity.setUniqueId(producerComponent.getUniqueId());
//    producerActivity.setParent(workflowActivity);
//    producerActivity.setClassName(producerComponent.getClassName());
//    producerActivity.setDestination(producerComponent.getDestination());
//    producerActivity.setVendorImpClass(producerComponent.getVendorImp());
//    return producerActivity;
//  }
//
//  private ServiceActivity buildServiceActivity(InterlokComponent serviceComponent, ServiceContainerActivity parentActivity) {
//    ServiceActivity serviceActivity = new ServiceActivity();
//    serviceActivity.setUniqueId(serviceComponent.getUniqueId());
//    serviceActivity.setParent(parentActivity);
//    serviceActivity.setClassName(serviceComponent.getClassName());
//    return serviceActivity;
//  }
//
//  private ServiceActivity buildServiceListActivity(InterlokComponent serviceComponent, ServiceContainerActivity parentActivity) {
//    ServiceListActivity serviceListActivity = new ServiceListActivity();
//    serviceListActivity.setUniqueId(serviceComponent.getUniqueId());
//    serviceListActivity.setParent(parentActivity);
//    serviceListActivity.setClassName(serviceComponent.getClassName());
//    return serviceListActivity;
//  }

//  private InterlokComponent findAdapterParent(InterlokComponent interlokComponent) {
//    return findParentOfType(interlokComponent, ComponentType.Adapter);
//  }
//
//  private InterlokComponent findChannelParent(InterlokComponent interlokComponent) {
//    return findParentOfType(interlokComponent, ComponentType.Channel);
//  }
//
//  private InterlokComponent findWorkflowParent(InterlokComponent interlokComponent) {
//    return findParentOfType(interlokComponent, ComponentType.Workflow);
//  }
//
//  private InterlokComponent findParentOfType(InterlokComponent interlokComponent, ComponentType componentType) {
//    InterlokComponent parent = interlokComponent;
//    do {
//      parent = parent.getParent();
//    } while (parent != null && !parent.getComponentType().equals(componentType));
//    return parent;
//  }
//
//  private long calculateAvgMsTaken(List<Long> msTakens) {
//    long totalTaken = 0;
//    for(long msTaken : msTakens) {
//      totalTaken += msTaken;
//    }
//    return totalTaken / msTakens.size();
//  }

  public Map<String, BaseActivity> getAdapters() {
    return adapters;
  }

  public void setAdapters(Map<String, BaseActivity> adapters) {
    this.adapters = adapters;
  }

  public void resetActivity() {
    for(String key : getAdapters().keySet()) {
      getAdapters().get(key).resetActivity();
    }
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for(BaseActivity adapter : getAdapters().values()) {
      buffer.append(adapter.toString());
    }
    return buffer.toString();
  }

}
