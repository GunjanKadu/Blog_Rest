package com.api.rest.IT;

import com.api.rest.BlogUser;
import com.api.rest.controllers.UserController;
import com.api.rest.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest
public class UserControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    public List<BlogUser> createUsers() {
        BlogUser John = userRepository.save(FixturesIT.User_John());
        BlogUser Bob = userRepository.save(FixturesIT.User_Bob());
        return List.of(John, Bob);
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void test_getAllUsers_IT() {
        createUsers();
        ResponseEntity<List<BlogUser>> users = userController.getUsers();
        Assertions.assertThat(users.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(users.getBody().size()).isEqualTo(2);
    }

    @Test
    public void test_getUser_IT() {
        List<BlogUser> createdUsers = createUsers();
        BlogUser userJohn = createdUsers.get(0);

        ResponseEntity<BlogUser> user = userController.getUser(userJohn.getId());

        Assertions.assertThat(user.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(user.getBody().getId()).isEqualTo(userJohn.getId());
    }

    @Test
    public void test_getUserNotFound_IT() {
        createUsers();
        ResponseEntity<BlogUser> user = userController.getUser(1L);
        Assertions.assertThat(user.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
