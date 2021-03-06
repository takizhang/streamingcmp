/*
 * Copyright (c) 2013 Yahoo! Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */

package com.yahoo.storm.yarn;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import org.apache.storm.utils.Utils;

public class Config {
  private static final Logger LOG = LoggerFactory.getLogger(Config.class);

  final public static String MASTER_DEFAULTS_CONFIG = "master_defaults.yaml";
  final public static String MASTER_CONFIG = "storm.yarn.master.yaml";
  final public static String MASTER_HOST = "master.host";
  final public static String MASTER_THRIFT_PORT = "master.thrift.port";
  final public static String MASTER_TIMEOUT_SECS = "master.timeout.secs";
  final public static String MASTER_SIZE_MB = "master.container.size-mb";
  final public static String MASTER_NUM_SUPERVISORS =
      "master.initial-num-supervisors";
  final public static String MASTER_CONTAINER_PRIORITY =
      "master.container.priority";
  final public static String MASTER_CONTAINER_RESTART_NUM =
      "master.container.restart-num";
  final public static String MASTER_CONTAINER_CALCULATOR =
      "master.container.calculator";
  final public static String MASTER_CONTAINER_CALCULATOR_CAPABILITY =
      "master.container.calculator.capability.max-workers-per-supervisor";
  final public static String MASTER_NIMBUS_RETRY_NUM =
      "master.nimbus.retry-num";
  final public static String MASTER_SUBMIT_SLEEP_SECS =
      "master.submit.sleep-secs";

  // # of milliseconds to wait for YARN report on Storm Master host/port
  final public static String YARN_REPORT_WAIT_MILLIS =
      "yarn.report.wait.millis";
  final public static String YARN_AM_RESPONSEID_FILE =
      "amclient_responseid.txt";
  final public static String MASTER_THRIFT_PORT_FILE = "master_thriftport.txt";
  final public static String SUPERVISOR_CONTAINER_RESTART_NUM =
      "supervisor.container.restart-num";

  final public static String CONTAINER_RELAUNCH_NUMBER_ENV =
      "CONTAINER_RELAUNCH_NUMBER";
  final public static String CONTAINER_RELAUNCH_NUMBER_DEFAULT = "-1";
  final public static String MASTER_HEARTBEAT_INTERVAL_MILLIS =
      "master.heartbeat.interval.millis";
  final public static String SUPERVISOR_NUM_WORKERS =
      "supervisor.initial-num-workers";
  final public static String WORKER_NUM_VCORE = "worker.initial-num-vcore";
  final public static String STORM_YARN_YARNCLASSPATH =
      "storm.yarn.yarn_classpath";
  final public static String STORM_YARN_JAVAHOME = "storm.yarn.java_home";

  final public static String MASTER_STORM_DRPC_SERVICE_ENABLE =
      "master.storm.drpc.service.enable";

  @SuppressWarnings("rawtypes")
  static public Map readStormConfig() {
    return readStormConfig(null);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  static Map readStormConfig(String stormYarnConfigPath) {
    // default configurations
    Map ret = Utils.readStormConfig();
    Map conf = Utils.findAndReadConfigFile(Config.MASTER_DEFAULTS_CONFIG);
    ret.putAll(conf);

    // configuration file per command parameter
    if (stormYarnConfigPath == null) {
      Map master_conf =
          Utils.findAndReadConfigFile(Config.MASTER_CONFIG, false);
      ret.putAll(master_conf);
      LOG.info("master_conf:" + master_conf.toString());
    } else {
      try {
        Yaml yaml = new Yaml();
        FileInputStream is = new FileInputStream(stormYarnConfigPath);
        Map storm_yarn_config = (Map) yaml.load(is);
        if (storm_yarn_config != null)
          ret.putAll(storm_yarn_config);
        is.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    // other configuration settings via CLS opts per system property:
    // storm.options
    ret.putAll(Utils.readCommandLineOpts());

    return ret;
  }

}
