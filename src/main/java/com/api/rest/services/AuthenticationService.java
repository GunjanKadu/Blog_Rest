package com.api.rest.services;

import com.api.rest.BlogUser;
import com.api.rest.Role;
import com.api.rest.config.JwtService;
import com.api.rest.controllers.AuthController.AuthenticationRequest;
import com.api.rest.controllers.AuthController.AuthenticationResponse;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.exceptions.ResourceExistsException;
import com.api.rest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = BlogUser.build(
                0L,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getAge(),
                Role.USER);

        Optional<BlogUser> existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new ResourceExistsException(String.format("%s is already registered", request.getEmail()));
        }
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
