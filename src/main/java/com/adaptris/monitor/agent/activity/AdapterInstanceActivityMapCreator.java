package com.adaptris.monitor.agent.activity;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.Adapter;
import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.AdaptrisMessageConsumer;
import com.adaptris.core.AdaptrisMessageProducer;
import com.adaptris.core.Channel;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceCollection;
import com.adaptris.core.Workflow;
import com.adaptris.core.WorkflowImp;

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
      traverseAdapter((AdapterActivity) activityObject, (AdaptrisComponent) object);
    } else {
      throw new RuntimeException("Cannot create an ActivityMap from an instance of " + object.getClass().getName());
    }

    return returnedMap;
  }

  /**
   * Make our way through the Adapter building an hierarchical map of each component.
   * @param activityObject
   * @param object
   */
  private void traverseAdapter(AdapterActivity parentActivityObject, AdaptrisComponent component) {

    for(Channel channel : ((Adapter) component).getChannelList()) {
      ChannelActivity channelActivity = (ChannelActivity) createActivityObject(channel);
      parentActivityObject.addChannelActivity(channelActivity);

      for(Workflow workflow : channel.getWorkflowList()) {
        WorkflowActivity workflowActivity = (WorkflowActivity) createActivityObject(workflow);
        channelActivity.addWorkflow(workflowActivity);

        ProducerActivity producerActivity = (ProducerActivity) createActivityObject(workflow.getProducer());
        producerActivity.setClassName(workflow.getProducer().getClass().getSimpleName());
        ConsumerActivity consumerActivity = (ConsumerActivity) createActivityObject(workflow.getConsumer());
        consumerActivity.setClassName(workflow.getConsumer().getClass().getSimpleName());

        workflowActivity.setConsumerActivity(consumerActivity);
        workflowActivity.setProducerActivity(producerActivity);

        for (Service service : ((WorkflowImp) workflow).getServiceCollection()) {
          ServiceActivity serviceActivity = createServiceActivity(service);
          workflowActivity.addServiceActivity(serviceActivity);

          traverseServiceForServices(serviceActivity, service);
        }
      }
    }
  }

  /**
   * Scan through the given service looking for child services to maintain a hierarchical component map.
   * @param serviceActivity
   * @param service
   */
  private void traverseServiceForServices(ServiceActivity serviceActivity, Service service) {
    try {
      for (Service childService : scanClassReflectiveAllGetters(service)) {
        ServiceActivity childServiceActivity = createServiceActivity(childService);
        serviceActivity.getServices().put(childServiceActivity.getUniqueId(), childServiceActivity);

        if(childService instanceof ServiceCollection) {
          traverseServiceForServices(childServiceActivity, childService);
        }
      }

    } catch (Throwable throwable) {
      log.error("Traversing Service has caused an error.", throwable);
    }
  }

  /**
   * Create a ServiceActivity implementation object which will later contain performance data for each service.
   *
   * @param Service
   * @return ServiceActivity
   */
  private ServiceActivity createServiceActivity(Service service) {
    ServiceActivity serviceActivity = (ServiceActivity) createActivityObject(service);
    serviceActivity.setClassName(service.getClass().getSimpleName());
    return serviceActivity;
  }

  /**
   * Will scan the given service for all getter methods and return the name of the getter with the getter return value
   * if and only if the returned getter value is of type Service.
   * @param service
   * @return
   */
  @SuppressWarnings("unchecked")
  // @VisibleForTesting
  List<Service> scanClassReflectiveAllGetters(Service service) {
    try {
      List<Service> map = new ArrayList<>();
      List<PropertyDescriptor> propertyDescriptors = Arrays.asList(Introspector.getBeanInfo(service.getClass(), Object.class).getPropertyDescriptors());
      propertyDescriptors.stream()
      .filter(pd -> Objects.nonNull(pd.getReadMethod()))  // filter out properties with setters only
      .forEach(pd -> { // invoke method to get value
        try {
          Object value = pd.getReadMethod().invoke(service);
          if (value != null) {
            if (value instanceof Service) {
              map.add((Service) value);
            }
            else if (value instanceof Collection<?>) { // attempt to catch all collection with Service generic type.
              Type methodReturnType = pd.getReadMethod().getGenericReturnType();
              // Handle collection with generics
              if (ParameterizedType.class.isInstance(methodReturnType)) {
                ParameterizedType genericReturnType = (ParameterizedType) methodReturnType;
                if (genericReturnType.getActualTypeArguments()[0] == Service.class) {
                  for (Service cService : (List<Service>) value) {
                    map.add(cService);
                  }
                }
              }
              // Handle plain collection ie without generics
              else {
                for (Object cService : (Collection)value) {
                  if (Service.class.isInstance(cService))
                    map.add((Service) cService);
                }
              }
            }
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      return map;
    } catch (IntrospectionException e) {
      log.error("Failed to get all child services", e);
      return Collections.emptyList();
    }
  }

  public void wtfisit(Type o) {
    System.out.println("Print given type is: " + o.getTypeName());
    if (o instanceof Collection) {
      System.out.println("Item is a collection");
    }
    else if (o instanceof  List) {
      System.out.println("Item is a list");
    }
    else {
      System.out.println("Unknown item");
    }
  }
  /**
   * Create the BaseActivity implementation object which will later contain performance data for each component.
   * @param AdaptrisComponent
   * @return BaseActivity
   */
  private BaseActivity createActivityObject(AdaptrisComponent adaptrisComponent) {
    BaseActivity returnedBaseActivity = null;
    if(adaptrisComponent instanceof Adapter) {
      returnedBaseActivity = new AdapterActivity();
    } else if (adaptrisComponent instanceof Channel) {
      returnedBaseActivity = new ChannelActivity();
    } else if (adaptrisComponent instanceof Workflow) {
      returnedBaseActivity = new WorkflowActivity();
    } else if (adaptrisComponent instanceof Service) {
      returnedBaseActivity = new ServiceActivity();
    } else if (adaptrisComponent instanceof AdaptrisMessageConsumer) {
      returnedBaseActivity = new ConsumerActivity();
    } else if (adaptrisComponent instanceof AdaptrisMessageProducer) {
      returnedBaseActivity = new ProducerActivity();
    }

    returnedBaseActivity.setUniqueId(adaptrisComponent.getUniqueId());

    if(componentIds.contains(adaptrisComponent.getUniqueId())) {
      log.warn("Component UniqueID clash; {}.\nProfiling may be compromised.", adaptrisComponent.getUniqueId());
    } else {
      componentIds.add(adaptrisComponent.getUniqueId());
    }

    return returnedBaseActivity;
  }
}
