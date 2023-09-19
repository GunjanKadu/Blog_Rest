package com.api.rest.services;

import com.api.rest.Role;

import com.api.rest.BlogUser;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.controllers.UserController.UserResponse;
import com.api.rest.exceptions.ResourceDoesNotExistsException;
import com.api.rest.exceptions.ResourceExistsException;
import com.api.rest.interfaces.IUserService;
import com.api.rest.repositories.UserRepository;
import com.api.rest.util.GlobalInfo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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
    public UserResponse editUser(BlogUser user) {
        Optional<BlogUser> optionalUserFromDB = userRepository.findByEmail(GlobalInfo.getBlogUser().getEmail());

        UserResponse userResponse = new UserResponse();

        if (optionalUserFromDB.isEmpty()) {
            throw new ResourceDoesNotExistsException(String.format("%s does not exist", user.getEmail()));
        }

        optionalUserFromDB.ifPresent(userFromDB -> {
            if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail()
                    .equals(userFromDB.getEmail())) {
                userFromDB.setEmail(user.getEmail());
            }
            if (user.getFirstName() != null && !user.getFirstName().isEmpty() && !user.getFirstName()
                    .equals(userFromDB.getFirstName())) {
                userFromDB.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null && !user.getLastName().isEmpty() && !user.getLastName()
                    .equals(userFromDB.getLastName())) {
                userFromDB.setLastName(user.getLastName());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                userFromDB.setPassword(encodedPassword);
            }
            if (user.getAge() > RegisterRequest.MINIMUM_AGE && user.getAge() < RegisterRequest.MAXIMUM_AGE) {
                userFromDB.setAge(user.getAge());
            }

            BlogUser updatedUser = userRepository.save(userFromDB);
            userResponse.setFirstName(updatedUser.getFirstName());
            userResponse.setLastName(updatedUser.getLastName());
            userResponse.setEmail(updatedUser.getEmail());
            userResponse.setAge(updatedUser.getAge());
            userResponse.setRole(updatedUser.getRole());
        });

        return userResponse;
    }

}
