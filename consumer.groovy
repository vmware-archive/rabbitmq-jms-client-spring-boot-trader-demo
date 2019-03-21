/*
 * Copyright (C) 2007-2016 Pivotal Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.rabbitmq.jms.sample

@Grab("com.rabbitmq.jms:rabbitmq-jms:1.7.0")

import com.rabbitmq.jms.admin.RMQConnectionFactory
import org.springframework.jms.listener.adapter.MessageListenerAdapter

@Configuration
@Log
@EnableJms
class StockConsumer {

  @Bean
  ConnectionFactory connectionFactory() {
    new RMQConnectionFactory()
  }

  @Bean
  DefaultMessageListenerContainer jmsListener(ConnectionFactory connectionFactory) {
    log.info "connectionFactory => ${connectionFactory}"
    new DefaultMessageListenerContainer([
      connectionFactory: connectionFactory,
      destinationName: "rabbit-trader-channel",
      pubSubDomain: false,
      messageListener: new MessageListenerAdapter(new Receiver()) {{
        defaultListenerMethod = "receive"
      }}
    ])
  }

}

@Log
class Receiver {
  def receive(String message) {
    log.info "Received tip ${message}"
  }
}
