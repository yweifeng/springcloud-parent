package ywf.service;

import org.springframework.stereotype.Component;

@Component
public class HelloServiceHystrix implements HelloService {
    @Override
    public String hello(String msg) {
        return "feign error " + msg;
    }
}
