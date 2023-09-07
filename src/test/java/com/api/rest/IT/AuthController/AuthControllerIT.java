package com.api.rest.IT.AuthController;

import com.api.rest.IT.BaseIT;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthControllerIT extends BaseIT {

    private AuthUtil authUtil;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        authUtil = new AuthUtil(getRestTemplate(), getBaseUrl());
    }

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void userCanRegisterSuccessfullyAndRecieveToken() {
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        ResponseEntity<Object> response = authUtil.registerUser(registerRequest);

        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);

        String token = body.get("token");
        assertNotNull(token);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 100);
    }

    @Test
    public void userCannotRegisterIfAlreadyRegistered() {
        RegisterRequest registerRequest = new RegisterRequest("Test_1", "User_2", "test1@gmail.com", "12345testuser",
                25);
        ResponseEntity<Object> response = authUtil.registerUser(registerRequest);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Object> response_new = authUtil.registerUser(registerRequest);
        Map<String, String> body_new = (Map<String, String>) response_new.getBody();
        assertNotNull(body_new);

        assertEquals(response_new.getStatusCode(), HttpStatus.CONFLICT);
        assertEquals(body_new.get("error"), String.format("%s is already registered", registerRequest.getEmail()));
    }

}
