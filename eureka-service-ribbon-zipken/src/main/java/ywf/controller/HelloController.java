package ywf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ywf.service.HelloService;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello(@RequestParam String msg) {
        System.out.println("zipken call hello " + msg);
        return helloService.hello(msg);
    }

    @RequestMapping("/info")
    public String info(@RequestParam String info) {
        System.out.println("zipken call info " + info);
        int i = 1/0;
        return "info = " + info;
    }


}
