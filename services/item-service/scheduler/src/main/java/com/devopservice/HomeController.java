package com.devopservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "Scheduler Service is running! Available endpoints: /api/workers, /api/shifts, /api/shift-assignments, /api/roles, /actuator/health";
    }
}