package com.api.rest.controllers;

import com.api.rest.BlogUser;
import com.api.rest.interfaces.IUserController;
import com.api.rest.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements IUserController {

    private IUserService userService;

    public UserController(@Autowired IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<BlogUser>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogUser> getUser(@PathVariable Long id) {
        try {
            BlogUser blogUser = userService.getUser(id).orElseThrow();
            return new ResponseEntity<>(blogUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<BlogUser> createUser(@RequestBody BlogUser user) {
        BlogUser createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<List<Map<String, MyClass>>> testing() {
        Map<String, MyClass> jwtTokens = new HashMap<>();
        jwtTokens.put("key-1", new MyClass("24March", "NotUpDated"));
        jwtTokens.put("key-2", new MyClass("12March", "NotUpDated"));
        jwtTokens.put("key-3", new MyClass("13March", "NotUpDated"));

//
//        Map<String, MyClass> formatted = new HashMap<>();
//
//        jwtTokens.keySet().forEach(k -> {
//            MyClass res = new MyClass(jwtTokens.get(k).getCreationTimeStamp(), jwtTokens.get(k).getCreationTimeStamp());
//            formatted.put(k, res);
//        });

        List<Map<String, MyClass>> collect = jwtTokens.keySet().stream().map(k -> {
            Map<String, MyClass> formatted1 = new HashMap<>();
            MyClass res = new MyClass(jwtTokens.get(k).getCreationTimeStamp(), jwtTokens.get(k).getCreationTimeStamp());
            formatted1.put(k, res);
            return formatted1;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }
}

