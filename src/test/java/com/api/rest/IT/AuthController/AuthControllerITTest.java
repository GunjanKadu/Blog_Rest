package com.api.rest.IT.AuthController;

import com.api.rest.IT.BaseITTest;
import com.api.rest.controllers.AuthController.AuthenticationRequest;
import com.api.rest.controllers.AuthController.AuthenticationResponse;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.repositories.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthControllerITTest extends BaseITTest {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserRepository userRepository;

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }

    private <RESP_T> ResponseEntity<RESP_T> registerUser(String firstName, String lastName,
            String email, String password,
            int age, Class<RESP_T> responseType) {
        RegisterRequest registerRequest = new RegisterRequest(firstName, lastName, email, password, age);
        ResponseEntity<RESP_T> response = authUtil.registerUser(registerRequest, responseType);
        return response;
    }

    @Test
    public void userCanRegisterSuccessfullyAndRecieveToken() {
        ResponseEntity<AuthenticationResponse> response = registerUser("Test", "User", "test@gmail.com",
                "12345testuser", 25, AuthenticationResponse.class);

        AuthenticationResponse body = response.getBody();
        assertNotNull(body);

        String token = body.getToken();
        assertNotNull(token);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 100);
    }

    @Test
    public void userCannotRegisterIfAlreadyRegistered() {
        String email = "test1@gmail.com";
        //register user once
        ResponseEntity<AuthenticationResponse> response = registerUser("Test_1", "User_2", email, "12345testuser", 25,
                AuthenticationResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        //Try to register with same email
        ResponseEntity<Map> response_new = registerUser("Test_1", "User_2", email, "12345testuser",
                25, Map.class);

        Map<String, String> body_new = (Map<String, String>) response_new.getBody();
        assertNotNull(body_new);

        assertEquals(response_new.getStatusCode(), HttpStatus.CONFLICT);
        assertEquals(body_new.get("error"), String.format("%s is already registered", email));
    }

    @Test
    public void userCannotRegisterWithIncorrectRequestBody() {
        ResponseEntity<Object> response = registerUser("", "", "test", "12345testuser", 15,
                Object.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        HashMap<String, String> body = (HashMap<String, String>) response.getBody();

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
        ResponseEntity<Object> response = registerUser("Test", "User_2", email, password, 25, Object.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        // Then login
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);
        ResponseEntity<AuthenticationResponse> loginResponse = authUtil.loginUser(authenticationRequest,
                AuthenticationResponse.class);
        assertEquals(loginResponse.getStatusCode(), HttpStatus.OK);

        assertNotNull(loginResponse.getBody());

        String token = loginResponse.getBody().getToken();
        assertNotNull(token);

        assertFalse(token.isEmpty());
        assertTrue(token.length() > 100);
    }

    @Test
    public void userCannotLoginWithoutEmailorPassword() {
        String email = "test3@gmail.com";
        String password = "12345testuser";
        ResponseEntity<Object> response = registerUser("Test", "User_2", email, password, 25, Object.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("", "");
        ResponseEntity<AuthenticationRequest> loginResponse = authUtil.loginUser(authenticationRequest,
                AuthenticationRequest.class);
        assertEquals(loginResponse.getStatusCode(), HttpStatus.BAD_REQUEST);

        assertNotNull(loginResponse.getBody());
        assertEquals(loginResponse.getBody().getPassword(), "Password Cannot be Null");
        assertEquals(loginResponse.getBody().getEmail(), "must not be blank");
    }

    @Test
    public void userCannotLoginWithIncorrectCredentials() {
        String email = "test3@gmail.com";
        String password = "12345testuser";
        ResponseEntity<Object> response = registerUser("Test", "User_2", email, password, 25, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, "wrongPassword");
        ResponseEntity<Map> loginResponse = authUtil.loginUser(authenticationRequest, Map.class);
        assertEquals(loginResponse.getStatusCode(), HttpStatus.FORBIDDEN);

        assertEquals(loginResponse.getBody().get("error"), "Bad credentials");
    }
}
