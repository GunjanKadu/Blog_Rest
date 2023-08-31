package com.api.rest.services;

import com.api.rest.BlogUser;
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

}
