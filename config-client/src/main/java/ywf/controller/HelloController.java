package ywf.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${foo}")
    private String foo;

    @RequestMapping("/hello")
    public String hello() {
        return foo;
    }
}
