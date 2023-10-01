package com.api.rest.IT.UserController;

import com.api.rest.BlogUser;
import com.api.rest.IT.AuthController.AuthUtil;
import com.api.rest.IT.BaseITTest;
import com.api.rest.Role;
import com.api.rest.controllers.AuthController.AuthenticationRequest;
import com.api.rest.controllers.AuthController.AuthenticationResponse;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.controllers.UserController.UserResponse;
import com.api.rest.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerITTest extends BaseITTest {
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test()
    public void testUserShouldGetSelfDetailsWhenLoggedIn() {
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        String token = registerUserAndReturnToken(registerRequest);

        ResponseEntity<UserResponse> me = userUtil.getMe(token, UserResponse.class);
        assertNotNull(me.getBody());
        UserResponse myDetails_body = me.getBody();

        assertNotNull(myDetails_body);
        assertEquals(myDetails_body.getFirstName(), registerRequest.getFirstName());
        assertEquals(myDetails_body.getLastName(), registerRequest.getLastName());
        assertEquals(myDetails_body.getAge(), registerRequest.getAge());
        assertEquals(myDetails_body.getEmail(), registerRequest.getEmail());
        assertEquals(myDetails_body.getRole(), Role.USER);
    }

    @Test
    public void testUserShouldNotGetSelfDetailsWhenNotLoggedIn() {
        ResponseEntity<String> bearer_invalid_token = userUtil.getMe("Bearer Invalid Token", String.class);
        assertNotNull(bearer_invalid_token.getBody());
        assertTrue(bearer_invalid_token.getBody().contains("Invalid Credentials"));
    }

    @Test
    public void testEditUserWhenAllParametersAreCorrect() throws IOException {
        // Create User and get token
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        String token = registerUserAndReturnToken(registerRequest);

        BlogUser userToEdit = new BlogUser();
        userToEdit.setFirstName("EditedFirstName");
        userToEdit.setLastName("EditedLastName");
        userToEdit.setAge(50);
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("editedEmail@gmail.com");
        HttpResponse httpResponse = userUtil.editMe(token, userToEdit);

        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        HashMap responseBodyMap = objectMapper.readValue(responseBody, HashMap.class);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.OK.value());
        assertEquals(responseBodyMap.get("firstName"), userToEdit.getFirstName());
        assertEquals(responseBodyMap.get("lastName"), userToEdit.getLastName());
        assertEquals(responseBodyMap.get("age"), userToEdit.getAge());
        assertEquals(responseBodyMap.get("email"), userToEdit.getEmail());
        assertEquals(responseBodyMap.get("role"), "USER");
    }

    @Test
    public void testEditUserWhenFirstNameLastNameAndEmailAreNotPresent() throws IOException {
        // Create User and get token
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        String token = registerUserAndReturnToken(registerRequest);

        BlogUser userToEdit = new BlogUser();
        userToEdit.setAge(50);
        userToEdit.setPassword("editedPassword");

        HttpResponse httpResponse = userUtil.editMe(token, userToEdit);

        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        HashMap responseBodyMap = objectMapper.readValue(responseBody, HashMap.class);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.OK.value());
        assertEquals(responseBodyMap.get("firstName"), registerRequest.getFirstName());
        assertEquals(responseBodyMap.get("lastName"), registerRequest.getLastName());
        assertEquals(responseBodyMap.get("age"), userToEdit.getAge());
        assertEquals(responseBodyMap.get("email"), registerRequest.getEmail());
        assertEquals(responseBodyMap.get("role"), "USER");
    }

    @Test
    public void testEditUserFirstNameLastNameAndEmailAreBlankAndAgeExceedsAllowedLimit() throws IOException {
        // Create User and get token
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        String token = registerUserAndReturnToken(registerRequest);

        BlogUser userToEdit = new BlogUser();
        userToEdit.setFirstName("");
        userToEdit.setLastName("");
        userToEdit.setAge(RegisterRequest.MAXIMUM_AGE + 1);
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("");

        HttpResponse httpResponse = userUtil.editMe(token, userToEdit);

        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        HashMap responseBodyMap = objectMapper.readValue(responseBody, HashMap.class);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.OK.value());
        assertEquals(responseBodyMap.get("firstName"), registerRequest.getFirstName());
        assertEquals(responseBodyMap.get("lastName"), registerRequest.getLastName());
        assertEquals(responseBodyMap.get("age"), registerRequest.getAge());
        assertEquals(responseBodyMap.get("email"), registerRequest.getEmail());
        assertEquals(responseBodyMap.get("role"), "USER");
    }

    @Test
    public void testEditUserWhenBlankFirstNameLastNameAndInvalidEmail() throws IOException {
        // Create User and get token
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        String token = registerUserAndReturnToken(registerRequest);

        BlogUser userToEdit = new BlogUser();
        userToEdit.setFirstName("");
        userToEdit.setLastName("");
        userToEdit.setAge(RegisterRequest.MAXIMUM_AGE + 1);
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("notvalidemail");

        HttpResponse httpResponse = userUtil.editMe(token, userToEdit);

        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        HashMap responseBodyMap = objectMapper.readValue(responseBody, HashMap.class);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.OK.value());
        assertEquals(responseBodyMap.get("firstName"), registerRequest.getFirstName());
        assertEquals(responseBodyMap.get("lastName"), registerRequest.getLastName());
        assertEquals(responseBodyMap.get("age"), registerRequest.getAge());
        assertEquals(responseBodyMap.get("email"), registerRequest.getEmail());
        assertEquals(responseBodyMap.get("role"), "USER");
    }

    @Test
    public void testEditUserPasswordAndLoginWithNewPassword() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        String token = registerUserAndReturnToken(registerRequest);

        BlogUser userToEdit = new BlogUser();
        userToEdit.setPassword("editedPassword");

        HttpResponse httpResponse = userUtil.editMe(token, userToEdit);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.OK.value());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(registerRequest.getEmail());
        authenticationRequest.setPassword(userToEdit.getPassword());

        ResponseEntity<AuthenticationResponse> response = authUtil.loginUser(authenticationRequest,
                AuthenticationResponse.class);

        String newToken = Objects.requireNonNull(response.getBody()).getToken();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(Objects.requireNonNull(newToken));

        ResponseEntity<UserResponse> me = userUtil.getMe(newToken, UserResponse.class);
        assertNotNull(me.getBody());
        UserResponse myDetails_body = me.getBody();

        assertNotNull(myDetails_body);
        assertEquals(myDetails_body.getFirstName(), registerRequest.getFirstName());
        assertEquals(myDetails_body.getLastName(), registerRequest.getLastName());
        assertEquals(myDetails_body.getAge(), registerRequest.getAge());
        assertEquals(myDetails_body.getEmail(), registerRequest.getEmail());
        assertEquals(myDetails_body.getRole(), Role.USER);
    }

    @Test
    public void testEditUserPasswordAndEmailAndThenLogin() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        String token = registerUserAndReturnToken(registerRequest);

        BlogUser userToEdit = new BlogUser();
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("newemail@gmail.com");

        HttpResponse httpResponse = userUtil.editMe(token, userToEdit);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.OK.value());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(userToEdit.getEmail());
        authenticationRequest.setPassword(userToEdit.getPassword());

        ResponseEntity<AuthenticationResponse> response = authUtil.loginUser(authenticationRequest,
                AuthenticationResponse.class);

        String newToken = Objects.requireNonNull(response.getBody()).getToken();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(Objects.requireNonNull(newToken));

        ResponseEntity<UserResponse> me = userUtil.getMe(newToken, UserResponse.class);
        assertNotNull(me.getBody());
        UserResponse myDetails_body = me.getBody();

        assertNotNull(myDetails_body);
        assertEquals(myDetails_body.getFirstName(), registerRequest.getFirstName());
        assertEquals(myDetails_body.getLastName(), registerRequest.getLastName());
        assertEquals(myDetails_body.getAge(), registerRequest.getAge());
        assertEquals(myDetails_body.getEmail(), userToEdit.getEmail());
        assertEquals(myDetails_body.getRole(), Role.USER);
    }

    private String registerUserAndReturnToken(RegisterRequest registerRequest) {
        ResponseEntity<AuthenticationResponse> response = authUtil.registerUser(registerRequest,
                AuthenticationResponse.class);

        assertNotNull(response.getBody());

        String token = response.getBody().getToken();
        assertNotNull(token);
        return token;
    }

    //TODO write iT test for edit user
    //TODO also write a test where you change email and password and then login
}
