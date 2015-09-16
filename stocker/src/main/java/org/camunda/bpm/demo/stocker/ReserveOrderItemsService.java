/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.demo.stocker;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.camunda.bpm.ext.sdk.CamundaClient;
import org.camunda.bpm.ext.sdk.TaskContext;
import org.camunda.bpm.ext.sdk.Worker;
import org.camunda.bpm.ext.sdk.WorkerRegistration;
import org.camunda.spin.SpinList;
import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.camunda.bpm.engine.variable.Variables.*;
import static org.camunda.spin.plugin.variable.SpinValues.*;

/**
 * @author Daniel Meyer
 *
 */
@Component
public class ReserveOrderItemsService {

  private static final Logger LOG = LoggerFactory.getLogger(ReserveOrderItemsService.class);

  @Autowired
  protected CamundaClient camundaClient;

  protected WorkerRegistration registration;

  @PostConstruct
  protected void registerWorker() {
    registration = camundaClient.registerWorker()
      .lockTime(120)
      .topicName("orderProcess:reserveOrderItems")
      .variableNames("order")
      .worker(new Worker() {
        public void doWork(TaskContext taskContext) {

          SpinJsonNode orderData = taskContext.getVariable("order");
          if(orderData == null) {
            taskContext.taskFailed("Variable 'order' does not exist.");
          }
          else {

            // set new order status
            orderData.prop("status", "RESERVED");

            // mark items as reserved
            SpinList<SpinJsonNode> orderItems = orderData.prop("orderItems").elements();
            for (SpinJsonNode orderItem : orderItems) {
              orderItem.prop("reserved", true);
            }

            taskContext.complete(createVariables().putValue("order", jsonValue(orderData)));
          }

        }
      }).build();
  }

  @PreDestroy
  protected void removeWorker() {
    registration.remove();
  }

}
