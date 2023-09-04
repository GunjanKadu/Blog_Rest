package com.api.rest.controllers;

import com.api.rest.BlogUser;
import com.api.rest.exceptions.ApiRequestException;
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

import java.util.List;
import java.util.Optional;


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
        Optional<BlogUser> existingUser = userService.getUserByEmail(user.getUsername());
        if (existingUser != null && existingUser.isPresent()) {
            throw new ApiRequestException(String.format("User with email %s already exists",existingUser.get().getUsername()));
        }
        BlogUser createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }
}

