package ywf.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    private RestTemplate restTemplate;

    public String hello(String msg) {
        String url = "http://service-zipken/info?info=" + msg;
        return restTemplate.getForObject(url, String.class);
    }

}
