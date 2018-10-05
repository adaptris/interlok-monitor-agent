# interlok-monitor-agent [![GitHub tag](https://img.shields.io/github/tag/adaptris/interlok-monitor-agent.svg)](https://github.com/adaptris/interlok-monitor-agent/tags) [![Build Status](https://travis-ci.org/adaptris/interlok-monitor-agent.svg?branch=develop)](https://travis-ci.org/adaptris/interlok-monitor-agent) [![codecov](https://codecov.io/gh/adaptris/interlok-monitor-agent/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-monitor-agent) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/a04aaca3525a4c9083e15be97e99baeb)](https://www.codacy.com/app/adaptris/interlok-monitor-agent?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=adaptris/interlok-monitor-agent&amp;utm_campaign=Badge_Grade)

The source code contains a client and server component.

## Pre-requisites


Use the build script to do an `gradle clean jar`.

This will build a jar file that should be copied into the lib directory of your Interlok installation.

Copy the `interlok-monitor-agent.properties` file into your "config" directory of your Interlok installation.

Copy the jar files (found in this projects lib directory) into the lib directory of your Interlok installation.



## The Client

You will need to start an Interlok instance up with the interlok-profiler.  Then create the file interlok-profiler.properties inside your "config" directory.
The properties file only needs a single property;
com.adaptris.profiler.plugin.factory=com.adaptris.monitor.agent.InterlokMonitorPluginFactory
