config-server(配置中心服务)
===

1、pom.xml
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
    
2、application.xml(配置git信息和文件目录)
---
    spring:
      application:
        name: config-server
      cloud:
        config:
          server:
            git:
              uri: https://github.com/yweifeng/springcloud-config
              ## 目录
              search-paths: respo
              # 开源无需用户名密码
              username:
              password:
          label: master
          
3、application.java配置注解 @EnableConfigServer
---

4、2种访问方式
---
    1、http://localhost:2222/user-dev.properties
    2、http://localhost:2222/user/dev
    
    
改造为高可用分布式配置中心
===

1、pom.xml
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

2、bootstrap.yml 添加注册中心地址
---
    server:
      port: 3333
    spring:
      application:
        name: config-client
      cloud:
        config:
          label: master
          profile: dev
          #      uri: http://localhost:2222/
          discovery:
            enabled: true
            service-id: config-server
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8888/eureka/
    
3、application.java 添加注解 @EnableEurekaClient
---