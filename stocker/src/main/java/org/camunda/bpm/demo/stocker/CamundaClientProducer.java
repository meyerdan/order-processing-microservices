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

import org.camunda.bpm.ext.sdk.CamundaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Daniel Meyer
 *
 */
@Configuration
public class CamundaClientProducer {

  @Value("${camunda.endpointUrl:http://192.168.88.216:8080/engine-rest}")
  protected String endpointUrl;

  protected CamundaClient client;

  @Bean(destroyMethod="close")
  public CamundaClient camundaClient() {
    CamundaClient client = CamundaClient.create()
      .endpointUrl(endpointUrl)
      .build();
    return client;
  }

}
