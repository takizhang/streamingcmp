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

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

public class EmbeddedZKServer {

  private static final Log LOG = LogFactory.getLog(EmbeddedZKServer.class);
  private NIOServerCnxnFactory zkFactory;
  private int zkport;

  void start() throws IOException, InterruptedException {
    LOG.info("Starting up embedded Zookeeper server");
    File localfile = new File("./target/zookeeper.data");
    ZooKeeperServer zkServer;
    zkServer = new ZooKeeperServer(localfile, localfile, 2000);
    for (zkport = 60000; true; zkport++)
      try {
        zkFactory = new NIOServerCnxnFactory();
        zkFactory.configure(new InetSocketAddress(zkport), -1);
        break;
      } catch (BindException e) {
        if (zkport == 65535)
          throw new IOException(
              "Fail to find a port for Zookeeper server to bind");
      }
    LOG.info("Zookeeper port allocated:" + zkport);
    zkFactory.startup(zkServer);
  }

  int port() {
    return zkport;
  }

  void stop() {
    LOG.info("shutdown embedded zookeeper server with port " + zkport);
    zkFactory.shutdown();
    zkFactory = null;
  }
}
