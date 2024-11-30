package com.example.JWT.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @PreAuthorize("hasRole('ADMIN')")
@GetMapping("/welcome")
    public String welcom(){
    return "welcome";
}

@GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
            public String onlyManager(){
        return "manager access";
            }
}
