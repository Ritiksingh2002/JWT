package com.example.JWT.Controller;

import com.example.JWT.Model.UserRegistrationRequest;
import com.example.JWT.Model.Users;
import com.example.JWT.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class UserController {
   @Autowired
   private UserService userService;

    @PostMapping("/register")

    public Users registerUser(@RequestBody UserRegistrationRequest request) {
        return userService.register(request);
    }
    @PostMapping("/login")
    public String login(@RequestBody Users user){
//        System.out.println(user);
            System.out.println("role in login form"+user.getRoles());
        return userService.verify(user);
    }
}
