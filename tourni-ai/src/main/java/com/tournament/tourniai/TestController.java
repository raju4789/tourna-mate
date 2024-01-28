package com.tournament.tourniai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello World!";
    }
}
