package com.api.rest.IT.AuthController;

import com.api.rest.IT.BaseIT;
import com.api.rest.controllers.AuthController.AuthenticationRequest;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.repositories.UserRepository;
import org.junit.After;
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

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserRepository userRepository;

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }

    private ResponseEntity<Object> registerUser(String firstName, String lastName, String email, String password,
            int age) {
        RegisterRequest registerRequest = new RegisterRequest(firstName, lastName, email, password, age);
        ResponseEntity<Object> response = authUtil.registerUser(registerRequest);
        return response;
    }

    @Test
    public void userCanRegisterSuccessfullyAndRecieveToken() {
        ResponseEntity<Object> response = registerUser("Test", "User", "test@gmail.com", "12345testuser", 25);

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
        String email = "test1@gmail.com";
        //register user once
        ResponseEntity<Object> response = registerUser("Test_1", "User_2", email, "12345testuser", 25);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        //Try to register with same email
        ResponseEntity<Object> response_new = registerUser("Test_1", "User_2", email, "12345testuser", 25);

        Map<String, String> body_new = (Map<String, String>) response_new.getBody();
        assertNotNull(body_new);

        assertEquals(response_new.getStatusCode(), HttpStatus.CONFLICT);
        assertEquals(body_new.get("error"), String.format("%s is already registered", email));
    }

    @Test
    public void userCannotRegisterWithIncorrectRequestBody() {
        ResponseEntity<Object> response = registerUser("", "", "test", "12345testuser", 15);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertNotNull(body);
        assertEquals(body.get("firstName"), "FirstName Cannot be Null");
        assertEquals(body.get("lastName"), "LastName Cannot be Null");
        assertEquals(body.get("age"), "must be greater than or equal to 18");
        assertEquals(body.get("email"), "Email Not valid");
    }

    @Test
    public void userCanLoginAndRecieveToken() {
        // Register user first
        String email = "test2@gmail.com";
        String password = "12345testuser";
        ResponseEntity<Object> response = registerUser("Test", "User_2", email, password, 25);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        // Then login
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);
        ResponseEntity<Object> loginResponse = authUtil.loginUser(authenticationRequest);
        assertEquals(loginResponse.getStatusCode(), HttpStatus.OK);

        Map<String, String> body = (Map<String, String>) loginResponse.getBody();
        assertNotNull(body);

        String token = body.get("token");
        assertNotNull(token);

        assertFalse(token.isEmpty());
        assertTrue(token.length() > 100);
    }

    @Test
    public void userCannotLoginWithoutEmailorPassword(){
        String email = "test3@gmail.com";
        String password = "12345testuser";
        ResponseEntity<Object> response = registerUser("Test", "User_2", email, password, 25);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("", "");
        ResponseEntity<Object> loginResponse = authUtil.loginUser(authenticationRequest);
        assertEquals(loginResponse.getStatusCode(), HttpStatus.BAD_REQUEST);

        Map<String, String> body = (Map<String, String>) loginResponse.getBody();

        assertNotNull(body);
        assertEquals(body.get("password"), "Password Cannot be Null");
        assertEquals(body.get("email"), "must not be blank");
    }

    @Test
    public void userCannotLoginWithIncorrectCredentials(){
        String email = "test3@gmail.com";
        String password = "12345testuser";
        ResponseEntity<Object> response = registerUser("Test", "User_2", email, password, 25);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, "wrongPassword");
        ResponseEntity<Object> loginResponse = authUtil.loginUser(authenticationRequest);
        assertEquals(loginResponse.getStatusCode(), HttpStatus.FORBIDDEN);

        Map<String, String> body = (Map<String, String>) loginResponse.getBody();
        assertEquals(body.get("error"), "Bad credentials");
    }
}
