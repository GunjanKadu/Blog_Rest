package com.api.rest.interfaces;

import com.api.rest.BlogUser;
import com.api.rest.controllers.UserController.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserController {
    public ResponseEntity<UserResponse> getMe();
}
