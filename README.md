# RabbitMQ JMS Client Demo on Spring Boot

This sample application demonstrates the [Rabbit JMS connector](http://blog.gopivotal.com/products/messaging-with-jms-and-rabbitmq) by producing and consuming stock quotes.

## Prerequisites 

To use it, you need to have RabbitMQ [installed](http://www.rabbitmq.com/download.html) and running
with the [JMS topic exchange plugin](https://github.com/rabbitmq/rabbitmq-jms-topic-exchange).

Assuming RabbitMQ is running in a local terminal:

```
$ rabbitmq-server

              RabbitMQ *.*.*. Copyright (C) 2007-2016 Pivotal Software, Inc.
  ##  ##      Licensed under the MPL.  See http://www.rabbitmq.com/
  ##  ##
  ##########  Logs: /usr/local/var/log/rabbitmq/rabbit@localhost.log
  ######  ##        /usr/local/var/log/rabbitmq/rabbit@localhost-sasl.log
  ##########
              Starting broker... completed with 6 plugins.
```

…you are now ready to proceed!

## Getting started… fast!

One of the fastest ways to get this demo going is using [Spring Boot's CLI](http://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-installing-spring-boot.html).

With Spring Boot CLI in `PATH`, run both parts of the demo with

    spring run consumer.groovy producer.groovy

It might take a minute or two to download some dependencies if this is the first time you've used Spring Boot. But after that, you should see something like this:

```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.3.5.RELEASE)

2016-05-31 13:41:53.831  INFO 58645 --- [       runner-0] o.s.boot.SpringApplication               : Starting application on localhost with PID 58645 (/Users/antares/.m2/repository/org/springframework/boot/spring-boot/1.3.5.RELEASE/spring-boot-1.3.5.RELEASE.jar started by antares in /Users/antares/Development/RabbitMQ/jms_boot_demo.git)
2016-05-31 13:41:53.834  INFO 58645 --- [       runner-0] o.s.boot.SpringApplication               : No active profile set, falling back to default profiles: default
2016-05-31 13:41:53.888  INFO 58645 --- [       runner-0] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5bff0172: startup date [Tue May 31 13:41:53 MSK 2016]; root of context hierarchy
2016-05-31 13:41:54.624  INFO 58645 --- [       runner-0] o.s.b.f.s.DefaultListableBeanFactory     : Overriding bean definition for bean 'connectionFactory' with a different definition: replacing [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=stockConsumer; factoryMethodName=connectionFactory; initMethodName=null; destroyMethodName=(inferred); defined in com.rabbitmq.jms.sample.StockConsumer] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=stockQuoter; factoryMethodName=connectionFactory; initMethodName=null; destroyMethodName=(inferred); defined in com.rabbitmq.jms.sample.StockQuoter]
2016-05-31 13:41:55.027  INFO 58645 --- [       runner-0] com.rabbitmq.jms.sample.StockConsumer    : connectionFactory => RMQConnectionFactory{user='guest', password=xxxxxxxx, host='localhost', port=5672, virtualHost='/', queueBrowserReadMax=0}
2016-05-31 13:41:55.222  INFO 58645 --- [       runner-0] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2016-05-31 13:41:55.230  INFO 58645 --- [       runner-0] o.s.c.support.DefaultLifecycleProcessor  : Starting beans in phase 2147483647
2016-05-31 13:41:55.326  INFO 58645 --- [       runner-0] o.s.boot.SpringApplication               : Started application in 1.768 seconds (JVM running for 4.115)
2016-05-31 13:41:55.389  INFO 58645 --- [pool-3-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...GD is now 94.55
2016-05-31 13:41:55.445  INFO 58645 --- [  jmsListener-1] com.rabbitmq.jms.sample.Receiver         : Received tip Quote...GD is now 94.55
2016-05-31 13:42:00.326  INFO 58645 --- [pool-3-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...BRK.B is now 112.45
2016-05-31 13:42:00.419  INFO 58645 --- [  jmsListener-1] com.rabbitmq.jms.sample.Receiver         : Received tip Quote...BRK.B is now 112.45
2016-05-31 13:42:05.325  INFO 58645 --- [pool-3-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...AAPL is now 455.07
2016-05-31 13:42:05.391  INFO 58645 --- [  jmsListener-1] com.rabbitmq.jms.sample.Receiver         : Received tip Quote...AAPL is now 455.07
2016-05-31 13:42:10.325  INFO 58645 --- [pool-3-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...GD is now 100.22
2016-05-31 13:42:10.361  INFO 58645 --- [  jmsListener-1] com.rabbitmq.jms.sample.Receiver         : Received tip Quote...GD is now 100.22
```

## Building a JAR

To produce a releasable JAR file run

    mvn clean package

This creates a JAR file that can be run from the command line:

    java -jar target/rabbit-jms-boot-demo-1.0.0-SNAPSHOT.jar

## License and Copyright

(c) Pivotal Software, Inc., 2007-2016.

This package, the RabbitMQ JMS client demo on Spring Boot, is double-licensed
under the Apache License version 2 ("ASL") and the Mozilla Public License
1.1 ("MPL").

See [LICENSE](./LICENSE).
