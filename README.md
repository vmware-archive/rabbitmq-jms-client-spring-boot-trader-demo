rabbit-jms-boot-demo
====================
This sample application demonstrates the [Rabbit JMS connector](http://blog.gopivotal.com/products/messaging-with-jms-and-rabbitmq) by producing and consuming stock quotes.

To use it, you need to have RabbitMQ [installed](http://www.rabbitmq.com/download.html) and running.

Assuming you have this up in one of your terminal windows:

```
$ rabbitmq-server 

              RabbitMQ 3.1.5. Copyright (C) 2007-2013 GoPivotal, Inc.
  ##  ##      Licensed under the MPL.  See http://www.rabbitmq.com/
  ##  ##
  ##########  Logs: /usr/local/var/log/rabbitmq/rabbit@localhost.log
  ######  ##        /usr/local/var/log/rabbitmq/rabbit@localhost-sasl.log
  ##########
              Starting broker... completed with 6 plugins.
```

…you are now ready to proceed!

Getting started…fast!
----------------------------
One of the fastest ways to get this demo going is using [Spring Boot's CLI](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-cli). That link will show you how to install it using multiple options (homebrew, gvm, …).

With Spring Boot, just try this.

```
$ spring run consumer.groovy producer.groovy
```

It might take a minute or two to download some dependencies if this is the first time you've used Spring Boot. But after that, you should see something like this:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::  (v0.5.0.BUILD-SNAPSHOT)

2013-10-25 09:15:42.044  INFO 68139 --- [ckQuoter.main()] com.rabbitmq.jms.sample.StockQuoter      : Starting StockQuoter on retina with PID 68139 (/Users/gturnquist/src/rabbit-jms-boot-demo/target/classes started by gturnquist)
2013-10-25 09:15:42.076  INFO 68139 --- [ckQuoter.main()] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@507d259: startup date [Fri Oct 25 09:15:42 CDT 2013]; root of context hierarchy
2013-10-25 09:15:42.343  INFO 68139 --- [ckQuoter.main()] o.s.b.f.s.DefaultListableBeanFactory     : Overriding bean definition for bean 'org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor': replacing [Root bean: class [org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null] with [Root bean: class [org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null]
2013-10-25 09:15:42.440  INFO 68139 --- [ckQuoter.main()] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.scheduling.annotation.SchedulingConfiguration' of type [class org.springframework.scheduling.annotation.SchedulingConfiguration$$EnhancerByCGLIB$$bdff0256] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2013-10-25 09:15:42.653  INFO 68139 --- [ckQuoter.main()] o.s.c.support.DefaultLifecycleProcessor  : Starting beans in phase 2147483647
2013-10-25 09:15:42.774  INFO 68139 --- [ckQuoter.main()] com.rabbitmq.jms.sample.StockQuoter      : Started StockQuoter in 1.097 seconds
2013-10-25 09:15:42.775  INFO 68139 --- [ol-141-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...AAPL is now 524.0
2013-10-25 09:15:42.776  INFO 68139 --- [ckQuoter.main()] com.rabbitmq.jms.sample.StockQuoter      : connectionFactory => com.rabbitmq.jms.admin.RMQConnectionFactory@7da0a60b
2013-10-25 09:15:42.887  INFO 68139 --- [  jmsListener-1] com.rabbitmq.jms.sample.StockConsumer    : Received Quote...AAPL is now 524.0
2013-10-25 09:15:47.772  INFO 68139 --- [ol-141-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...AAPL is now 503.0
2013-10-25 09:15:47.788  INFO 68139 --- [  jmsListener-1] com.rabbitmq.jms.sample.StockConsumer    : Received Quote...AAPL is now 503.0
2013-10-25 09:15:52.782  INFO 68139 --- [ol-141-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...GD is now 93.0
2013-10-25 09:15:52.884  INFO 68139 --- [  jmsListener-1] com.rabbitmq.jms.sample.StockConsumer    : Received Quote...GD is now 93.0
2013-10-25 09:15:57.772  INFO 68139 --- [ol-141-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...BRK.B is now 113.0
2013-10-25 09:15:57.866  INFO 68139 --- [  jmsListener-1] com.rabbitmq.jms.sample.StockConsumer    : Received Quote...BRK.B is now 113.0
2013-10-25 09:16:02.772  INFO 68139 --- [ol-141-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...GD is now 92.0
2013-10-25 09:16:02.839  INFO 68139 --- [  jmsListener-1] com.rabbitmq.jms.sample.StockConsumer    : Received Quote...GD is now 92.0
```

In the midst of all that output, you should notice this:

```
2013-10-25 09:15:42.776  INFO 68139 --- [ckQuoter.main()] com.rabbitmq.jms.sample.StockQuoter      : connectionFactory => com.rabbitmq.jms.admin.RMQConnectionFactory@7da0a60b
```

This demonstrations that the connection factory is `RMQConnectionFactory`, the key piece of the Rabbit JMS project.

Moving to big time production
-----------------------------
Okay, that was neat. With less than 100 lines of Groovy code, you saw a nice demo of production/consumption through JMS. But perhaps you need something a little more scalable for a big application. For starters, that Groovy application up above requires access to the Internet and also contains the super secret location of the Rabbit JMS client. These would be key reasons you can't just hand the code to anyone.

So…how about building a deliverable artifact using Maven? Here we have a demonstration of using this code in pure Java.

```
$ mvn clean spring-boot:run
```

This will use Maven to build and launch the application as well.

```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::  (v0.5.0.BUILD-SNAPSHOT)

2013-10-25 09:26:24.510  INFO 68721 --- [ckQuoter.main()] com.rabbitmq.jms.sample.StockQuoter      : Starting StockQuoter on retina with PID 68721 (/Users/gturnquist/src/rabbit-jms-boot-demo/target/classes started by gturnquist)
2013-10-25 09:26:24.555  INFO 68721 --- [ckQuoter.main()] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@4bdb635a: startup date [Fri Oct 25 09:26:24 CDT 2013]; root of context hierarchy
2013-10-25 09:26:24.797  INFO 68721 --- [ckQuoter.main()] o.s.b.f.s.DefaultListableBeanFactory     : Overriding bean definition for bean 'org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor': replacing [Root bean: class [org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null] with [Root bean: class [org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null]
2013-10-25 09:26:24.886  INFO 68721 --- [ckQuoter.main()] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.scheduling.annotation.SchedulingConfiguration' of type [class org.springframework.scheduling.annotation.SchedulingConfiguration$$EnhancerByCGLIB$$82fdc4dd] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2013-10-25 09:26:25.080  INFO 68721 --- [ckQuoter.main()] o.s.c.support.DefaultLifecycleProcessor  : Starting beans in phase 2147483647
2013-10-25 09:26:25.187  INFO 68721 --- [ckQuoter.main()] com.rabbitmq.jms.sample.StockQuoter      : Started StockQuoter in 1.027 seconds
2013-10-25 09:26:25.187  INFO 68721 --- [pool-3-thread-1] com.rabbitmq.jms.sample.StockQuoter      : Quote...BRK.B is now 107.0
2013-10-25 09:26:25.188  INFO 68721 --- [ckQuoter.main()] com.rabbitmq.jms.sample.StockQuoter      : connectionFactory => com.rabbitmq.jms.admin.RMQConnectionFactory@4167dd87
2013-10-25 09:26:25.323  INFO 68721 --- [  jmsListener-1] com.rabbitmq.jms.sample.StockConsumer    : Received Quote...BRK.B is now 107.0
```

Pretty much the same stuff.

Finally, you can build a releasable JAR file.

```
$ mvn clean package
```

This creates a JAR file that you can hand out as well as run from the command line.

```
$ java -jar target/rabbit-jms-boot-demo-1.0.4-SNAPSHOT.jar
```

Version issues
--------------
This code is clearly using Rabbit JMS 1.0.4 even though 1.1.0 is out. I couldn't get all the right artifacts to download from maven central with 1.1.0, so I backed it up to a point where it DID work.

Another nugget of dependency management: it has both Spring Boot and Rabbit JMS as parent dependencies. Maven only lets you have one parent, so this little work around was used.

The project's parent dependency is spring-boot-starter-parent. That lets it pull in default versions of things like Spring JMS without having to spec the versions by leaning on Spring Boot's choices.

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>0.5.0.BUILD-SNAPSHOT</version>
    </parent>
```

But it also gets stuff from Rabbit JMS.

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.rabbitmq.jms</groupId>
                <artifactId>rabbitmq-jms-parent</artifactId>
                <version>${rabbitmq-jms.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

This should theoretically let you pull in whatever rabbitmq-jms-parent says in extra dependency management.

> **Note:** I'm not a maven expert, so if this is off kilter, please advise or send a pull request.

Help
----
If you need help, contact either Steve Powell or Greg Turnquist for questions.
