/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package backtype.storm.command;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.tencent.jstorm.utils.CoreUtil;

/**
 * 
 * @author <a href="mailto:caofangkun@gmail.com">caokun</a>
 * @author <a href="mailto:xunzhang555@gmail.com">zhangxun</a>
 * 
 */
public class kill_topology {

  @Option(name = "--help", aliases = { "-h" }, usage = "print help message")
  private boolean _help = false;

  @Option(name = "--name", aliases = {
      "--topologyName" }, metaVar = "NAME", usage = "name of the topology")
  private String _name = "test";

  @Option(name = "--wait", aliases = {
      "-w" }, usage = "wait seconds default is 5")
  private int _wait = 5;

  private static void printUsage() {
    System.out.println("Usage:");
    System.out.println(
        "    $STORM_HOME/bin/storm kill --name topology_name --wait wait_secs");
  }

  @SuppressWarnings("rawtypes")
  public void realMain(String[] args) throws Exception {
    CmdLineParser parser = new CmdLineParser(this);
    parser.setUsageWidth(80);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      _help = true;
    }
    if (_help) {
      parser.printUsage(System.err);
      System.err.println();
      return;
    }

    if (_name == null || _name.isEmpty()) {
      throw new IllegalArgumentException("privide topology name please");
    }

    try {
      CoreUtil.killTopology(_name, _wait);
    } catch (Exception e) {
      System.out.println(CoreUtil.stringifyError(e));
      printUsage();
    }
  }

  public static void main(String[] args) throws Exception {
    new kill_topology().realMain(args);
  }

}
