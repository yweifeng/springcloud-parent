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
