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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

@SpringBootApplication
@EnableScheduling
public class StockQuoter {

	private static final Log log = LogFactory.getLog(StockQuoter.class);

	private List<String> stocks = new ArrayList<String>();
	private Map<String, Double> lastPrice = new HashMap<String, Double>();

	{
		stocks.add("AAPL");
		stocks.add("GD");
		stocks.add("BRK.B");

		lastPrice.put("AAPL", 494.64);
		lastPrice.put("GD", 86.74);
		lastPrice.put("BRK.B", 113.59);
	}

	@Autowired
	JmsTemplate jmsTemplate;

	@Bean
	ConnectionFactory connectionFactory() {
		return new RMQConnectionFactory();
	}

	@Scheduled(fixedRate = 5000L) // every 5 seconds
	public void publishQuote() {

		// Pick a random stock symbol
		Collections.shuffle(stocks);
		String symbol = stocks.get(0);

		// Toss a coin and decide if the price goes...
		if (RandomUtils.nextBoolean()) {
			// ...up by a random 0-10%
			lastPrice.put(symbol, new Double(Math.round(lastPrice.get(symbol) * (1 + RandomUtils.nextInt(10) / 100.0) * 100) / 100));
		}
		else {
			// ...or down by a similar random amount
			lastPrice.put(symbol, new Double(Math.round(lastPrice.get(symbol) * (1 - RandomUtils.nextInt(10) / 100.0) * 100) / 100));
		}

		// Log new price locally
		log.info("Quote..." + symbol + " is now " + lastPrice.get(symbol));

		// Coerce a javax.jms.MessageCreator
		MessageCreator messageCreator = (Session session) -> {
			return session.createObjectMessage(
					"Quote..." + symbol + " is now " + lastPrice.get(symbol));
		};

		// And publish to RabbitMQ using Spring's JmsTemplate
		jmsTemplate.send("rabbit-trader-channel", messageCreator);
	}

	public static void main(String[] args) {
		SpringApplication.run(StockQuoter.class, args);
	}

}
