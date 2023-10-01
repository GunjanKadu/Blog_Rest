package com.api.rest.Unit;

import com.api.rest.BlogUser;
import com.api.rest.Role;
import com.api.rest.controllers.UserController.UserController;
import com.api.rest.controllers.UserController.UserResponse;
import com.api.rest.util.GlobalInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    private MockedStatic<GlobalInfo> globalInfoMockedStatic;

    @Before
    public void setUp() {
        globalInfoMockedStatic = mockStatic(GlobalInfo.class);
    }

    @After
    public void tearDown(){
        globalInfoMockedStatic.close();
    }

    @Test
    public void getOwnDetailsWhenUserAuthenticated() {

        BlogUser blogUser = new BlogUser();
        blogUser.setFirstName("Test");
        blogUser.setLastName("User");
        blogUser.setEmail("testuser@gmail.com");
        blogUser.setAge(20);
        blogUser.setRole(Role.USER);

        globalInfoMockedStatic.when(()->GlobalInfo.getBlogUser()).thenReturn(blogUser);

        ResponseEntity<UserResponse> me = userController.getMe();

        UserResponse body = me.getBody();
        assert body != null;
        assertEquals(body.getAge(), blogUser.getAge());
        assertEquals(body.getEmail(), blogUser.getEmail());
        assertEquals(body.getFirstName(), blogUser.getFirstName());
        assertEquals(body.getLastName(), blogUser.getLastName());
        assertEquals(body.getRole(), blogUser.getRole());
    }

}