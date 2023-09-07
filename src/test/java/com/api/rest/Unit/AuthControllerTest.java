package com.api.rest.Unit;

import com.api.rest.controllers.AuthController.AuthController;
import com.api.rest.controllers.AuthController.AuthenticationRequest;
import com.api.rest.controllers.AuthController.AuthenticationResponse;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.services.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    private Validator validator;

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        // For javax validations
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void userCanSuccessfullyRegister() {
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "test@gmail.com", "12345testuser", 25);

        when(authenticationService.register(registerRequest)).thenReturn(
                AuthenticationResponse.builder().token("register_token").build());

        ResponseEntity<AuthenticationResponse> response = authController.register(registerRequest);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()).getToken(), "register_token");
    }

    @Test
    public void userCannotRegisterWhenIncorrectRequestBody() {
        RegisterRequest registerRequest = new RegisterRequest(null, "User", "test", null, 2);

        Set<ConstraintViolation<RegisterRequest>> validate = validator.validate(registerRequest);
        Assertions.assertFalse(validate.isEmpty());
    }

    @Test
    public void userCanSuccessfullyLogin() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUser@gmail.com", "ghjkzuiohj");

        when(authenticationService.authentication(authenticationRequest)).thenReturn(
                AuthenticationResponse.builder().token("login_token").build());

        ResponseEntity<AuthenticationResponse> response = authController.login(authenticationRequest);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()).getToken(), "login_token");
    }

    @Test
    public void userCannotLoginWithoutEmailorPassword() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("test", "ghjkzuiohj");

        Set<ConstraintViolation<AuthenticationRequest>> validate = validator.validate(authenticationRequest);
        Assertions.assertFalse(validate.isEmpty());
    }
}
