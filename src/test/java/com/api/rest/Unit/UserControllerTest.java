package com.api.rest.Unit;

import com.api.rest.BlogUser;
import com.api.rest.controllers.UserController.UserController;
import com.api.rest.services.UserService;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    private List<BlogUser> users() {
        return List.of(Fixtures.User_John(), Fixtures.User_Tom());
    }

    @Test
    public void test_getAllUsers() {
        when(userService.getUsers()).thenReturn(users());
        ResponseEntity<List<BlogUser>> blogUsers = userController.getUsers();
        Assertions.assertThat(blogUsers.getBody().size()).isEqualTo(users().size());
    }

    @Test
    public void test_getUser() {
        when(userService.getUser(anyLong())).thenReturn(Optional.of(Fixtures.User_John()));
        ResponseEntity<BlogUser> user = userController.getUser(1L);
        Assertions.assertThat(user.getBody().getId()).isEqualTo(1L);
    }

    @Test
    public void test_getUserNotFound() {
        when(userService.getUser(anyLong())).thenReturn(null);
        ResponseEntity<BlogUser> user = userController.getUser(1L);
        Assertions.assertThat(user.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
