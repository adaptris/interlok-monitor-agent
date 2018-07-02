package com.adaptris.monitor.agent.activity;

import java.util.Map;

public interface ServiceContainerActivity extends Activity {

  void addServiceActivity(ServiceActivity serviceActivity);

  Map<String, ServiceActivity> getServices();

  void setServices(Map<String, ServiceActivity> services);

}
