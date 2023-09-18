package com.api.rest.IT.UserController;

import com.api.rest.IT.AuthController.AuthUtil;
import com.api.rest.IT.BaseITTest;
import com.api.rest.Role;
import com.api.rest.controllers.AuthController.AuthenticationResponse;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.controllers.UserController.UserResponse;
import com.api.rest.repositories.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

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

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test()
    public void testUserShouldGetSelfDetailsWhenLoggedIn() {
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);
        ResponseEntity<AuthenticationResponse> response = authUtil.registerUser(registerRequest, AuthenticationResponse.class);

        assertNotNull(response.getBody());

        String token = response.getBody().getToken();
        assertNotNull(token);

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
}
