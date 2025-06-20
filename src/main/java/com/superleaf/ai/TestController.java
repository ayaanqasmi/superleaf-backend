package com.superleaf.ai;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/ai")
public class TestController {

    @GetMapping
    public String getGreeting(@RequestParam(required = false, defaultValue = "there") String param) {
        return "Hello, " + param + "! Skrrr!";
    }

}
