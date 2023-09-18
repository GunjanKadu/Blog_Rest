package com.api.rest.services;

import com.api.rest.BlogUser;
import com.api.rest.controllers.UserController.UserEditRequest;
import com.api.rest.controllers.UserController.UserResponse;
import com.api.rest.exceptions.ResourceExistsException;
import com.api.rest.interfaces.IUserService;
import com.api.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<BlogUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<BlogUser> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public BlogUser createUser(BlogUser user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<BlogUser> getUserByEmail(String email) {
        Optional<BlogUser> blogUser = userRepository.findByEmail(email);
        if (blogUser.isPresent()) {
            return blogUser;
        }
        return null;
    }

    @Override
    public Optional<UserResponse> editUser(BlogUser user) {
        Optional<BlogUser> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isEmpty()) {
            throw new ResourceExistsException(String.format("%s does not exists", user.getEmail()));
        }


        return Optional.empty();
    }

}
