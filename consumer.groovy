package com.rabbitmq.jms.sample

@Grab("com.rabbitmq.jms:rabbitmq-jms:1.4.1-SNAPSHOT")

import com.rabbitmq.jms.admin.RMQConnectionFactory

@Configuration
@EnableJmsMessaging
class StockConsumer {

  @Bean
  ConnectionFactory connectionFactory() {
    new RMQConnectionFactory()
  }

  @Bean
  DefaultMessageListenerContainer jmsListener(ConnectionFactory connectionFactory) {
    new DefaultMessageListenerContainer([
      connectionFactory: connectionFactory,
      destinationName: "rabbit-trader",
      pubSubDomain: true,
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
