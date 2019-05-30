# springcloud-parent
1、学习maven父子模块化
2、学习springcloud-eureka-server、springcloud-client、spring-service-ribbon(负载均衡)的使用
	注意事项：
	1、***application中配置 @SpringBootApplication 默认扫描当前包和子包
	2、不同点：@EnableEurekaClient只适用于Eureka作为注册中心，@EnableDiscoveryClient 可以是其他注册中心
	3、负载均衡: @LoadBalanced 
		注解LoadBalanced对Ribben进行了封装，所以这就叫做ribbon+restTemplate实现负载均衡，详情看地址https://blog.csdn.net/qq_26562641/article/details/53332269
		@Bean
		@LoadBalanced
		RestTemplate restTemplate() {
			return new RestTemplate();
		}

1、Controller注解必须使用@RestController才能访问到。
2、pom.xml scrope:
	* compile，缺省值，适用于所有阶段，会随着项目一起发布。 
	* provided，类似compile，期望JDK、容器或使用者会提供这个依赖。如servlet.jar。 
	* runtime，只在运行时使用，如JDBC驱动，适用运行和测试阶段。 
	* test，只在测试时使用，用于编译和运行测试代码。不会随项目发布。 
	* system，类似provided，需要显式提供包含依赖的jar，Maven不会在Repository中查找它。
	
	import 适用于 type=pom 放在dependencyManagement中，解决单独依赖

	@loadBalanced: 封装了feign  实现负载均衡以及可以通过applicaiton.name的方式进行访问

	LoadBalancerClient是一个接口，该接口中有四个方法，我们来大概看一下这几个方法的作用：

	ServiceInstance choose(String serviceId)根据传入的服务名serviceId从客户端负载均衡器中挑选一个对应服务的实例。
	T execute() ,使用从负载均衡器中挑选出来的服务实例来执行请求。


feign:注意事项（默认也具有负载均衡功能）
	1、pom.xml 2.x版本以后使用open-feign而不是starter-feign
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>
	2、Application上添加注解@EnableFeignClients
		@SpringBootApplication
		@EnableDiscoveryClient
		@EnableEurekaClient
		@EnableFeignClients
		public class ServiceFeignApplication {
			public static void main(String[] args) {
				SpringApplication.run(ServiceFeignApplication.class, args);
			}
		}
	3、service interface上添加@FeignClient(value="/clientname") --clientname 对应服务serviceId
		
		调用方法  注意@RequestParam("msg") msg必填
		@FeignClient(value = "c1")
		@Repository
		public interface HelloService {

			@RequestMapping(value = "/hello", method = RequestMethod.GET)
			String hello(@RequestParam("msg") String msg);
		}
	feign和ribbon+rest的比较
	feign：
	1. feign本身里面就包含有了ribbon
	2. feign自身是一个声明式的伪http客户端，写起来更加思路清晰和方便
	3. fegin是一个采用基于接口的注解的编程方式，更加简便

hystrix
	ribbon集成hystrix
		1、pom.xml
			（方式一）
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-starter-hystrix</artifactId>
			</dependency>
			（方式二、可以试用dashboard）
			<dependency>
				<groupId>com.netflix.hystrix</groupId>
				<artifactId>hystrix-javanica</artifactId>
				<version>RELEASE</version>
			</dependency>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-netflix-hystrix-dashboard</artifactId>
			</dependency>
		2、application添加@EnableHystrix注解
		3、对应service方法添加@HystrixCommand(fallCallback="方法")
			@HystrixCommand(fallbackMethod = "handleError")
			public String hello(String msg) {
				String url = "http://c1/hello?msg=" + msg;
				return restTemplate.getForObject(url, String.class);
			}

			public String handleError(String msg) {
				return "error " + msg;
			}
	feign:
		1、application.yml 开启hystrix
			feign:
			  hystrix:
				enabled: true
		2、接口FeignClient 添加 fallback
			@FeignClient(value = "c1", fallback = HelloServiceHystrix.class)
			@Repository
			public interface HelloService {
				@RequestMapping(value = "/hello", method = RequestMethod.GET)
				String hello(@RequestParam("msg") String msg);
			}
		3、实现HelloServiceHystrix类
			@Component
			public class HelloServiceHystrix implements HelloService {
				@Override
				public String hello(String msg) {
					return "feign error " + msg;
				}
			}

hystrix-dashboard(ribbon和feign一样)
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
		2、Application添加注解和注册serveltBearn
		
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
		3、访问url:http://localhost:9999/hystrix
		   输入 http://localhost:9999/hystrix.stream

	

	
