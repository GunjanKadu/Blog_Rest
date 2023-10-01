package com.api.rest.interfaces;

import com.api.rest.BlogUser;
import com.api.rest.controllers.UserController.UserEditRequest;
import com.api.rest.controllers.UserController.UserResponse;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<BlogUser> getUsers();

    Optional<BlogUser> getUser(Long id);

    BlogUser createUser(BlogUser user);
    Optional<BlogUser> getUserByEmail(String email);

    UserResponse editUser(BlogUser user);


}
