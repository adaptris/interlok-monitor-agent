package com.adaptris.monitor.agent.activity;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.Adapter;
import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.AdaptrisMessageConsumer;
import com.adaptris.core.AdaptrisMessageProducer;
import com.adaptris.core.Channel;
import com.adaptris.core.Service;
import com.adaptris.core.Workflow;

public class AdapterInstanceActivityMapCreator implements ActivityMapCreator {
  
  protected transient Logger log = LoggerFactory.getLogger(this.getClass());
  
  // Keep track of unique-ids we have seen, then we can for actual uniqueness.
  private List<String> componentIds;

  /**
   * Given an Adapter instance, we will traverse the instance creating a hierarchical ActivityMap.
   */
  @Override
  public ActivityMap createBaseMap(Object object) {
    componentIds = new ArrayList<>();
    
    ActivityMap returnedMap = new ActivityMap();
    
    if(object instanceof Adapter) {
      BaseActivity activityObject = createActivityObject((AdaptrisComponent) object);
      returnedMap.getAdapters().put(activityObject.getUniqueId(), activityObject);
      traverse(activityObject, (AdaptrisComponent) object);
    } else 
      throw new RuntimeException("Cannot create an ActivityMap from an instance of " + object.getClass().getName());
    
    return returnedMap;
  }

  /**
   * Make our way through the AdaptrisComponent, checking for child components.
   * @param activityObject
   * @param object
   */
  private void traverse(BaseActivity parentActivityObject, AdaptrisComponent component) {
    // TODO Auto-generated method stub
    
  }

  /**
   * Create the BaseActivity implementation object which will later contain performance data for each component.
   * @param AdaptrisComponent
   * @return BaseActivity
   */
  private BaseActivity createActivityObject(AdaptrisComponent adaptrisComponent) {
    BaseActivity returnedBaseActivity = null;
    if(adaptrisComponent instanceof Adapter)
      returnedBaseActivity = new AdapterActivity();
    else if (adaptrisComponent instanceof Channel)
      returnedBaseActivity = new ChannelActivity();
    else if (adaptrisComponent instanceof Workflow)
      returnedBaseActivity = new WorkflowActivity();
    else if (adaptrisComponent instanceof Service)
      returnedBaseActivity = new ServiceActivity();
    else if (adaptrisComponent instanceof AdaptrisMessageConsumer)
      returnedBaseActivity = new ConsumerActivity();
    else if (adaptrisComponent instanceof AdaptrisMessageProducer)
      returnedBaseActivity = new ProducerActivity();
    
    returnedBaseActivity.setUniqueId(adaptrisComponent.getUniqueId());
    
    if(componentIds.contains(adaptrisComponent.getUniqueId()))
      log.warn("Component UniqueID clash; {}.\nProfiling may be compromised.", adaptrisComponent.getUniqueId()); 
    else
      componentIds.add(adaptrisComponent.getUniqueId());
    
    return returnedBaseActivity;
  }

}
