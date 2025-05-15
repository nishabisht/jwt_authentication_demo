package com.demo.jwt_auth.controller;

import com.demo.jwt_auth.model.Users;
import com.demo.jwt_auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<Users> getUser(){
        return userService.getAllUser();
    }

    @PostMapping("/register")
    public Users registered(@RequestBody Users user){
        return  userService.saveUser(user);
    }

    //step 2
    @PostMapping("/login")
    public String login(@RequestBody Users user){
        System.out.println(user);
        return "Jwt token after successful login : "+userService.verify(user);
    }

}
