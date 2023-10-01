package com.api.rest.controllers.UserController;

import com.api.rest.BlogUser;
import com.api.rest.interfaces.IUserController;
import com.api.rest.interfaces.IUserService;
import com.api.rest.util.GlobalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
public class UserController implements IUserController {

    private final IUserService userService;

    public UserController(@Autowired IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getMe() {
        BlogUser blogUser = GlobalInfo.getBlogUser();
        UserResponse userResponse = new UserResponse(blogUser.getFirstName(), blogUser.getLastName(),
                blogUser.getEmail(), blogUser.getAge(), blogUser.getRole());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    // Edit User Details
    @PatchMapping()
    public ResponseEntity<UserResponse> editMe(@RequestBody BlogUser blogUser) {
        UserResponse userResponse = userService.editUser(blogUser);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}

