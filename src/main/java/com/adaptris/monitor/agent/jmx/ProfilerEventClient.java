package com.adaptris.monitor.agent.jmx;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.CoreException;
import com.adaptris.core.cache.ExpiringMapCache;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.util.TimeInterval;

public class ProfilerEventClient implements ProfilerEventClientMBean {
  
  protected transient Logger log = LoggerFactory.getLogger(this.getClass());

  private static final int DEFAULT_MAX_EVENT_HISTORY = 100;
  
  private static final long DEFAULT_EXPIRY = 10;
  
  private int maxEventHistory = 0;
  
  private ExpiringMapCache eventCache;
  
  public ProfilerEventClient() {
    
  }

  public int getMaxEventHistory() {
    return maxEventHistory;
  }

  public void setMaxEventHistory(int maxEventHistory) {
    this.maxEventHistory = maxEventHistory;
  }
  
  public int maxEventHistory() {
    return this.getMaxEventHistory() > 0 ? this.getMaxEventHistory() : DEFAULT_MAX_EVENT_HISTORY;
  }
  
  public int getEventCount() throws CoreException {
    return this.getEventCache().size();
  }
  
  public void addEventActivityMap(ActivityMap activityMap) throws CoreException {
    this.getEventCache().put(Long.toString(System.currentTimeMillis()), activityMap);
  }
  
  public List<ActivityMap> getEventActivityMaps() throws CoreException {
    return this.getEventCache().getKeys().stream().map( key -> {
      try {
        return (ActivityMap) getEventCache().get(key);
      } catch (CoreException e) {
        return null;
      }
    }).filter(item -> item instanceof ActivityMap).collect(Collectors.toList());
  }

  public ExpiringMapCache getEventCache() throws CoreException {
    if(eventCache == null) {
      setEventCache(new ExpiringMapCache()
          .withExpiration(new TimeInterval(DEFAULT_EXPIRY, TimeUnit.SECONDS))
          .withMaxEntries(maxEventHistory()));
      
      eventCache.init();
    }
    return eventCache;
  }

  public void setEventCache(ExpiringMapCache eventCache) {
    this.eventCache = eventCache;
  }
  
}
