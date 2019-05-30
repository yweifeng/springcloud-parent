package ywf.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "c1", fallback = HelloServiceHystrix.class)
@Repository
public interface HelloService {


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    String hello(@RequestParam("msg") String msg);
}
