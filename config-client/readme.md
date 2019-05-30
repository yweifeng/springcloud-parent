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