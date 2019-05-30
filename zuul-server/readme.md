zuul
===

功能和作用: 路由和过滤器
---

1、pom.xml
---
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>

2、application.yml添加路由配置
---
    zuul:
      routes:
        feign:
          path: /feign/**
          serviceId: service-feign
        ribbon:
          path: /ribbon/**
          serviceId: service-ribbon
3、application添加注解@EnableZuulProxy
---

4、自定义过滤器
---
    package ywf.filter;
    
    import com.netflix.zuul.ZuulFilter;
    import com.netflix.zuul.context.RequestContext;
    import org.apache.commons.lang.StringUtils;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Component;
    
    import javax.servlet.http.HttpServletRequest;
    
    @Component
    public class MyFilter extends ZuulFilter {
        private static Logger log = LoggerFactory.getLogger(MyFilter.class);
    
        @Override
        public String filterType() {
            /**
             * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
             * pre：路由之前
             * routing：路由之时
             * post： 路由之后
             * error：发送错误调用
             */
            return "pre";
        }
    
        @Override
        public int filterOrder() {
            return 0;
        }
    
        @Override
        public boolean shouldFilter() {
            return true;
        }
    
        /**
         * 过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。
         * @return
         */
        @Override
        public Object run() {
            // 获取Token
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletRequest request = context.getRequest();
            String token = request.getParameter("token");
            if (StringUtils.isBlank(token)) {
                log.warn("token is empty");
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(401);
                try {
                    context.getResponse().getWriter().write("token is empty");
                } catch (Exception e) {
                }
                return null;
            }
            return true;
        }
    }


