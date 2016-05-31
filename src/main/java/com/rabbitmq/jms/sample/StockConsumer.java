/*
 * Copyright (C) 2007-2016 Pivotal Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.rabbitmq.jms.sample;

import javax.jms.ConnectionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

@Configuration
public class StockConsumer {

	private static final Log log = LogFactory.getLog(StockConsumer.class);

	@Bean
	public DefaultMessageListenerContainer jmsListener(ConnectionFactory connectionFactory) {

		log.info("connectionFactory => " + connectionFactory);

		DefaultMessageListenerContainer jmsListener = new DefaultMessageListenerContainer();
		jmsListener.setConnectionFactory(connectionFactory);
		jmsListener.setDestinationName("rabbit-trader-channel");
		jmsListener.setPubSubDomain(false);

		MessageListenerAdapter adapter = new MessageListenerAdapter(new Receiver());
		adapter.setDefaultListenerMethod("receive");

		jmsListener.setMessageListener(adapter);
		return jmsListener;
	}

	static class Receiver {
		public void receive(String message) {
			log.info("Received " + message);
		}
	}
}
