package com.api.rest.controllers;

import com.api.rest.interfaces.IUserService;
import com.api.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private IUserService userService;

    public LoginController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void login(@RequestBody HashMap<String,String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");


    }
}
