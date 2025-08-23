package com.devopservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "User Service is running! Available endpoints: /api/auth/register, /api/auth/login, /actuator/health";
    }
}
