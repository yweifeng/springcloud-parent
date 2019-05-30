config-client 读取配置信息
===

1、pom.xml
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    
2、bootstrap.yml(注意不是application.yml)
---
    server:
      port: 3333
    spring:
      application:
        ## 这里对应 config-client-dev.properties
        name: config-client
      cloud:
        config:
          label: master
          profile: dev
          uri: http://localhost:2222/

3、读取配置
---
    @Value("${foo}")
    private String foo;
    
    
改造为高可用分布式配置中心
===

1、pom.xml
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

2、application.yml 添加注册中心地址
---
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8888/eureka/
    
3、application.java 添加注解 @EnableEurekaClient
---

集成消息总线(先装rabbitmq)
===

1、pom.xml
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-amqp</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

2、新增application.yml
---
    spring:
      rabbitmq:
        host: localhost
        port: 5672
        username: guest
        password: guest
      cloud:
        bus:
          enabled: true
          trace:
            enabled: true
    management:
      endpoints:
        web:
          exposure:
            include: bus-refresh

3、Bean.java (需要重新加载配置信息的类) 添加注解 
---
    @RestController
    @RefreshScope
    public class HelloController {
    
        @Value("${foo}")
        private String foo;
    
        @RequestMapping("/hello")
        public String hello() {
            return foo;
        }
    }

4、使用postman 发送post请求 http://localhost:3333/actuator/bus-refresh
---