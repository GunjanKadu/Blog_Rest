package com.api.rest.controllers;

import com.api.rest.BlogUser;
import com.api.rest.interfaces.IUserController;
import com.api.rest.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

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
    public ResponseEntity<BlogUser> getUser(@PathVariable Long userId) {
        try {
            BlogUser blogUser = userService.getUser(userId).orElseThrow();
            return new ResponseEntity<>(blogUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
