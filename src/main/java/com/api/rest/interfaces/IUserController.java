package com.api.rest.interfaces;

import com.api.rest.BlogUser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserController {
    public ResponseEntity<List<BlogUser>> getUsers();
}
