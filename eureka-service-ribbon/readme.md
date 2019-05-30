ribbon+rest+hystrix+dashedHystrix
===
1、pom.xml
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </dependency>
    
2、application添加restTemplate注入
---
    //注解LoadBalanced对Ribben进行了封装，所以这就叫做ribbon+restTemplate实现负载均衡，详情看地址https://blog.csdn.net/qq_26562641/article/details/53332269
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
3、调用方式(直接用serviceId访问)
---
    @Service
    public class HelloService {
        @Autowired
        private RestTemplate restTemplate;
        public String hello(String msg) {
            String url = "http://c1/hello?msg=" + msg;
            return restTemplate.getForObject(url, String.class);
        }
    }

添加断路器hystrix
===
1、pom.xml添加依赖
    <dependency>
        <groupId>com.netflix.hystrix</groupId>
        <artifactId>hystrix-javanica</artifactId>
        <version>RELEASE</version>
    </dependency>
2、application添加注解@EnableHystrix
3、为添加熔断方法
    @Service
    public class HelloService {
        @Autowired
        private RestTemplate restTemplate;
        @HystrixCommand(fallbackMethod = "handleError")
        public String hello(String msg) {
            String url = "http://c1/hello?msg=" + msg;
            return restTemplate.getForObject(url, String.class);
        }
        public String handleError(String msg) {
            return "error " + msg;
        }
    }
    
添加断路器监控 hystrix-dashboard
===
1、pom.xml添加依赖
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
    @EnableEurekaClient
    @SpringBootApplication
    @EnableHystrix
    @EnableHystrixDashboard
    public class ServiceRibbonApplication {
        public static void main(String[] args) {
            SpringApplication.run(ServiceRibbonApplication.class, args);
        }
        @Bean
        @LoadBalanced
        RestTemplate restTemplate() {
            return new RestTemplate();
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

    
    
    
 