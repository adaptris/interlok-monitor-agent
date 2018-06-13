package com.adaptris.monitor.agent.activity;

import com.adaptris.profiler.aspects.InterlokComponent;
import com.adaptris.profiler.aspects.InterlokComponent.ComponentType;

public class Utils {

  /**
   * Obtain the top level Adapter by climbing the hierarchy from the passed in component
   * @param interlokComponent - current component being examined
   * @return Enclosing Adapter instance
   */
  public static InterlokComponent getAdapterComponentFromService(InterlokComponent interlokComponent) {
    if (interlokComponent == null) return null;
    else if (interlokComponent != null && interlokComponent.getComponentType().equals(ComponentType.Adapter)) {
      return interlokComponent;
    } else {
      return getAdapterComponentFromService(interlokComponent.getParent());
    }
  }
  
  /**
   * Obtain the enclosing Channel by climbing the hierarchy from the passed in component
   * @param interlokComponent - current component being examined
   * @return Enclosing Channel instance
   */
  public static InterlokComponent getChannelComponentFromService(InterlokComponent interlokComponent) {
    if (interlokComponent == null) return null;
    else if (interlokComponent != null && interlokComponent.getComponentType().equals(ComponentType.Channel)) {
      return interlokComponent;
    } else {
      return getChannelComponentFromService(interlokComponent.getParent());
    }
  }
  
  /**
   * Obtain the enclosing Workflow by climbing the hierarchy from the passed in component
   * @param interlokComponent - current component being examined
   * @return Enclosing Channel instance
   */
  public static InterlokComponent getWorkflowComponentFromService(InterlokComponent interlokComponent) {
    if (interlokComponent == null) return null;
    else if (interlokComponent != null && interlokComponent.getComponentType().equals(ComponentType.Workflow)) {
      return interlokComponent;
    } else {
      return getWorkflowComponentFromService(interlokComponent.getParent());
    }
  }
}
