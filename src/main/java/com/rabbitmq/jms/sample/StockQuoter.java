package com.rabbitmq.jms.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

@EnableAutoConfiguration
@EnableScheduling
@Configuration
@ComponentScan
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
    final String symbol = stocks.get(0);

    // Toss a coin and decide if the price goes...
    if (RandomUtils.nextBoolean()) {
      // ...up by a random 0-10%
      lastPrice.put(symbol, new Double(Math.round(lastPrice.get(symbol) * (1 + RandomUtils.nextInt(10)/100.0) * 100) / 100));
    } else {
      // ...or down by a similar random amount
      lastPrice.put(symbol, new Double(Math.round(lastPrice.get(symbol) * (1 - RandomUtils.nextInt(10)/100.0) * 100) / 100));
    }

    // Log new price locally
    log.info("Quote..." + symbol + " is now " + lastPrice.get(symbol));

    MessageCreator messageCreator = new MessageCreator() {
      @Override
      public Message createMessage(Session session) throws JMSException {
        return session.createObjectMessage("Quote..." + symbol + " is now " + lastPrice.get(symbol));
      }
    };

    jmsTemplate.send("rabbit-trader-channel", messageCreator);
  }

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(StockQuoter.class, args);
    log.info("connectionFactory => " + ctx.getBean("connectionFactory"));
  }

}
