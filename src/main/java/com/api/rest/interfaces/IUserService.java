package com.api.rest.interfaces;

import com.api.rest.BlogUser;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<BlogUser> getUsers();

    Optional<BlogUser> getUser(Long id);

    BlogUser createUser(BlogUser user);
}
