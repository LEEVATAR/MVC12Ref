package com.model2.mvc.web.Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class RestApiController {

    @GetMapping("/data")
    public String getData() {
        return "Hello, world!";
    }
}