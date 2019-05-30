feign
===
1、pom.xml(2.x版本以后使用spring-cloud-starter-openfeign)
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    
2、application添加注解@EnableFeignClients
---
3、调用方式(注意:@RequestParam("msg")需要指定column)
---
    @FeignClient(value = "c1")
    @Repository
    public interface HelloService {
    
        @RequestMapping(value = "/hello", method = RequestMethod.GET)
        String hello(@RequestParam("msg") String msg);
    }

添加断路器hystrix
===
1、pom.xml添加依赖
---
    <dependency>
        <groupId>com.netflix.hystrix</groupId>
        <artifactId>hystrix-javanica</artifactId>
        <version>RELEASE</version>
    </dependency>
2、application.yml启动hystrix
---
    feign:
      hystrix:
        enabled: true
3、为添加熔断方法和实现类
---
    @FeignClient(value = "c1", fallback = HelloServiceHystrix.class)
    @Repository
    public interface HelloService {
    
        @RequestMapping(value = "/hello", method = RequestMethod.GET)
        String hello(@RequestParam("msg") String msg);
    }
        
    @Component
    public class HelloServiceHystrix implements HelloService {
        @Override
        public String hello(String msg) {
            return "feign error " + msg;
        }
    }
    
添加断路器监控 hystrix-dashboard
===
1、pom.xml添加依赖
---
    <dependency>
        <groupId>com.netflix.hystrix</groupId>
        <artifactId>hystrix-javanica</artifactId>
        <version>RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-netflix-hystrix-dashboard</artifactId>
    </dependency>
2、application添加注解和servlet注入
---
    @SpringBootApplication
    @EnableDiscoveryClient
    @EnableFeignClients
    @EnableHystrixDashboard
    public class ServiceFeignApplication {
        public static void main(String[] args) {
            SpringApplication.run(ServiceFeignApplication.class, args);
        }
    
        // 此配置是为了服务监控而配置，与服务容错本身无关，
        // ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
        // 只要在自己的项目里配置上下面的servlet就可以了
        @Bean
        public ServletRegistrationBean getServlet() {
            HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
            ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
            registrationBean.setLoadOnStartup(1);
            registrationBean.addUrlMappings("/hystrix.stream");
            registrationBean.setName("HystrixMetricsStreamServlet");
            return registrationBean;
        }
    }

    
    
    
 
