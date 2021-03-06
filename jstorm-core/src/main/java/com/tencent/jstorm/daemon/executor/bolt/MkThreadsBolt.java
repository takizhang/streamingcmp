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
package com.tencent.jstorm.daemon.executor.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tencent.jstorm.ClojureClass;
import com.tencent.jstorm.daemon.executor.ExecutorData;
import com.tencent.jstorm.daemon.task.TaskData;
import com.tencent.jstorm.utils.thread.RunnableCallback;

/**
 * 
 * @author <a href="mailto:caofangkun@gmail.com">caokun</a>
 * @author <a href="mailto:xunzhang555@gmail.com">zhangxun</a>
 * @author <a href="mailto:gulele2003@qq.com">gulele</a>
 * @author <a href="mailto:darezhong@qq.com">liuyuzhong</a>
 * @author <a href="mailto:fuangguang@126.com">wangfangguang</a>
 * @ModifiedBy kuncao
 * @ModifiedTime 5:09:24 PM Feb 25, 2016
 */
@ClojureClass(className = "org.apache.storm.daemon.executor#mk-threads#bolt")
public class MkThreadsBolt extends RunnableCallback {
  private static final long serialVersionUID = 1L;
  private static final Logger LOG =
      LoggerFactory.getLogger(MkThreadsBolt.class);
  private BoltFn boltFn;

  public MkThreadsBolt(ExecutorData executorData,
      Map<Integer, TaskData> taskDatas, Map<String, String> initialCredentials)
          throws Exception {

    LOG.info("Preparing bolt {}:{}", executorData.getComponentId(),
        taskDatas.keySet().toString());
    this.boltFn = new BoltFn(executorData, initialCredentials, taskDatas);
    LOG.info("Prepared bolt {}:{}", executorData.getComponentId(),
        taskDatas.keySet().toString());
  }

  @Override
  public BoltFn call() throws Exception {
    return boltFn;
  }
}
