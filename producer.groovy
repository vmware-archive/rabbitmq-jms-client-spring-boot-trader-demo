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

package com.rabbitmq.jms.sample

@Grab("com.rabbitmq.jms:rabbitmq-jms:1.4.7")

@Grab("commons-lang:commons-lang:2.6")

import com.rabbitmq.jms.admin.RMQConnectionFactory
import org.apache.commons.lang.math.RandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@EnableScheduling
@Log
@EnableJms
class StockQuoter {

  def stocks = ["AAPL", "GD", "BRK.B"] // stock symbols
  def lastPrice = ["AAPL": 494.64, "GD": 86.74, "BRK.B": 113.59] // starting prices

  @Autowired
  JmsTemplate jmsTemplate

  @Bean
  ConnectionFactory connectionFactory() {
    new RMQConnectionFactory()
  }

  @Scheduled(fixedRate = 5000L)
  void publishQuote() {
    // Pick a random stock symble
    Collections.shuffle(stocks, new Random())
    def symbol = stocks[0]

    // Toss a coin and decide if the price goes...
    if (RandomUtils.nextBoolean()) {
      // ...up by a random 0% - 10%
      lastPrice[symbol] = Math.round(lastPrice[symbol] * (1 + RandomUtils.nextInt(10)/100.0) * 100) / 100
    } else {
      // ...or down by a similar random amount
      lastPrice[symbol] = Math.round(lastPrice[symbol] * (1 - RandomUtils.nextInt(10)/100.0) * 100) / 100
    }

    // Log new price locally
    log.info "Quote...${symbol} is now ${lastPrice[symbol]}"

    // Coerce a javax.jms.MessageCreator
    def messageCreator = { session ->
      session.createObjectMessage("Quote...${symbol} is now ${lastPrice[symbol]}".toString())
    } as MessageCreator

    // And publish to RabbitMQ using Spring's JmsTemplate
    jmsTemplate.send("rabbit-trader-channel", messageCreator)
  }

}

