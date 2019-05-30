Spring Cloud Sleuth 服务链路追踪
===

1、下载zipken.jar包并启动
---
    启动成功后访问 http://localhost:9411/zipkin/

2、pom.xml
---
    <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-zipkin</artifactId>
     </dependency>   

3、注入Sampler 到spring容器
---
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

4、url访问接口、并通过浏览器观察链路
---