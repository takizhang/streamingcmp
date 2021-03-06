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
package com.tencent.jstorm.daemon.executor.grouping;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.storm.grouping.LoadMapping;
import org.apache.storm.task.WorkerTopologyContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tencent.jstorm.utils.CoreUtil;

/**
 * @author <a href="mailto:caofangkun@gmail.com">caokun</a>
 * @author <a href="mailto:xunzhang555@gmail.com">zhangxun</a>
 * @author <a href="mailto:gulele2003@qq.com">gulele</a>
 * @author <a href="mailto:darezhong@qq.com">liuyuzhong</a>
 * @author <a href="mailto:fuangguang@126.com">wangfangguang</a>
 * @ModifiedBy zionwang
 * @ModifiedTime 10:21:28 AM Feb 23, 2016
 */
public class LocalOrShuffleGrouper implements IGrouper {

  private ShuffleGrouper shffleGrouper;

  @SuppressWarnings("rawtypes")
  public LocalOrShuffleGrouper(WorkerTopologyContext context,
      List<Integer> targetTasks, Map topoConf, String componentId,
      String streamId) {

    Set<Integer> sameTasks =
        CoreUtil.SetIntersection(Sets.newHashSet(targetTasks),
            Sets.newHashSet(context.getThisWorkerTasks()));
    if (!sameTasks.isEmpty()) {
      shffleGrouper = new ShuffleGrouper(Lists.newArrayList(sameTasks),
          topoConf, context, componentId, streamId);
    } else {
      shffleGrouper = new ShuffleGrouper(targetTasks, topoConf, context,
          componentId, streamId);
    }

  }

  @Override
  public List<Integer> fn(Integer taskId, List<Object> values,
      LoadMapping load) {
    return shffleGrouper.fn(taskId, values, load);
  }

}
