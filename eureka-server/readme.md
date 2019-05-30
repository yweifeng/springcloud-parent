eureka-server(注册中心)
===

1、pom.xml
---
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>

2、application.yml 添加配置(不向注册中心注册自己)
---
    eureka:
      instance:
        hostname: localhost
      client:
        fetch-registry: false
        register-with-eureka: false
        service-url:
          defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

3、application.java 添加注解 @EnableEurekaServer
---

