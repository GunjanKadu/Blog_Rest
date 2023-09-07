package com.api.rest.controllers.UserController;

import com.api.rest.BlogUser;
import com.api.rest.interfaces.IUserController;
import com.api.rest.interfaces.IUserService;
import com.api.rest.util.GlobalInfo;
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

    private final IUserService userService;

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

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        BlogUser blogUser = GlobalInfo.getBlogUser();
        UserResponse userResponse = new UserResponse(blogUser.getFirstName(), blogUser.getLastName(),
                blogUser.getEmail(), blogUser.getAge(), blogUser.getRole());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}

