package com.rabbitmq.jms.sample

@Grab("com.rabbitmq.jms:rabbitmq-jms:1.4.7-SNAPSHOT")

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
