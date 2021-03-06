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
package com.tencent.jstorm.daemon.nimbus.transitions;

import com.tencent.jstorm.daemon.nimbus.NimbusData;
import com.tencent.jstorm.daemon.nimbus.NimbusUtils;
import com.tencent.jstorm.utils.thread.BaseCallback;

import org.apache.storm.generated.StormBase;
import org.apache.storm.generated.TopologyStatus;

public class DoRebalanceTransitionCallback extends BaseCallback {

  private String stormId;
  private TopologyStatus status;
  private StormBase stormBase;
  private NimbusData data;

  public DoRebalanceTransitionCallback(NimbusData nimbusData, String stormId,
      TopologyStatus status, StormBase stormBase) {
    this.data = nimbusData;
    this.stormId = stormId;
    this.stormBase = stormBase;
    this.status = status;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Object execute(T... args) {
    synchronized (data.getSubmitLock()) {
      NimbusUtils.doRebalance(data, stormId, status, stormBase);
    }
    // e.g. active --(reblance)-> reblancing --(delay:do_reblance)-> active
    stormBase.set_status(stormBase.get_prev_status());
    return stormBase;
  }
}
