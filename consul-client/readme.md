consul 注册中心
===

1、pom.xml
##
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>  

2、application.yml
##
    server:
      port: 4101
    spring:
      application:
        name: consul-client
      cloud:
        consul:
          host: localhost
          port: 8500
          discovery:
            service-name: consul-client

3、application.java 添加注解 @EnableDiscoveryClient
##