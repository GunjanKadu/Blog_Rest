package com.api.rest.Unit.Controllers;

import com.api.rest.BlogUser;
import com.api.rest.Role;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.controllers.UserController.UserController;
import com.api.rest.controllers.UserController.UserResponse;
import com.api.rest.exceptions.ResourceDoesNotExistsException;
import com.api.rest.repositories.UserRepository;
import com.api.rest.services.UserService;
import com.api.rest.util.GlobalInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private UserController userController;

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    private final BlogUser defaultBlogUser = new BlogUser();
    private MockedStatic<GlobalInfo> globalInfoMockedStatic;

    @Before
    public void setUp() {
        globalInfoMockedStatic = mockStatic(GlobalInfo.class);
        userService = new UserService(userRepository, passwordEncoder);
        userController = new UserController(userService);

        defaultBlogUser.setFirstName("Test");
        defaultBlogUser.setLastName("User");
        defaultBlogUser.setEmail("testuser@gmail.com");
        defaultBlogUser.setAge(20);
        defaultBlogUser.setRole(Role.USER);

        globalInfoMockedStatic.when(GlobalInfo::getBlogUser).thenReturn(defaultBlogUser);
    }

    @After
    public void tearDown() {
        globalInfoMockedStatic.close();
    }

    @Test
    public void getOwnDetailsWhenUserAuthenticated() {
        ResponseEntity<UserResponse> me = userController.getMe();

        UserResponse body = me.getBody();
        assert body != null;
        assertEquals(body.getAge(), defaultBlogUser.getAge());
        assertEquals(body.getEmail(), defaultBlogUser.getEmail());
        assertEquals(body.getFirstName(), defaultBlogUser.getFirstName());
        assertEquals(body.getLastName(), defaultBlogUser.getLastName());
        assertEquals(body.getRole(), defaultBlogUser.getRole());
    }

    @Test
    public void canEditUserWihtAllCorrectParameters() {
        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.of(defaultBlogUser));

        BlogUser userToEdit = new BlogUser();
        userToEdit.setFirstName("EditedFirstName");
        userToEdit.setLastName("EditedLastName");
        userToEdit.setAge(50);
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("editedEmail@gmail.com");

        when(passwordEncoder.encode(userToEdit.getPassword())).thenReturn("EncodedPassword");
        when(userRepository.save(any())).thenReturn(defaultBlogUser);

        ResponseEntity<UserResponse> response = userController.editMe(userToEdit);
        // service related assertions
        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");

        // response related assertions
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        UserResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(body.getEmail(), userToEdit.getEmail());
        assertEquals(body.getFirstName(), userToEdit.getFirstName());
        assertEquals(body.getLastName(), userToEdit.getLastName());
        assertEquals(body.getAge(), userToEdit.getAge());

    }

    @Test
    public void testEditUserWhenFirstNameLastNameAndEmailAreNotPresent() {
        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.of(defaultBlogUser));

        String oldLastName = defaultBlogUser.getLastName();
        String oldFirstName = defaultBlogUser.getFirstName();
        String oldEmail = defaultBlogUser.getEmail();

        BlogUser userToEdit = new BlogUser();
        userToEdit.setAge(50);
        userToEdit.setPassword("editedPassword");

        when(passwordEncoder.encode(userToEdit.getPassword())).thenReturn("EncodedPassword");
        when(userRepository.save(any())).thenReturn(defaultBlogUser);

        ResponseEntity<UserResponse> response = userController.editMe(userToEdit);

        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");

        // response related assertions
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        UserResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(body.getEmail(), oldEmail);
        assertEquals(body.getFirstName(), oldFirstName);
        assertEquals(body.getLastName(), oldLastName);
        assertEquals(body.getAge(), userToEdit.getAge());
    }

    @Test
    public void testEditUserFirstNameLastNameAndEmailAreBlankAndAgeExceedsAllowedLimit() {
        String oldLastName = defaultBlogUser.getLastName();
        String oldFirstName = defaultBlogUser.getFirstName();
        String oldEmail = defaultBlogUser.getEmail();
        int oldAge = defaultBlogUser.getAge();

        BlogUser userToEdit = new BlogUser();
        userToEdit.setFirstName("");
        userToEdit.setLastName("");
        userToEdit.setAge(RegisterRequest.MAXIMUM_AGE + 1);
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("");

        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.of(defaultBlogUser));
        when(passwordEncoder.encode(userToEdit.getPassword())).thenReturn("EncodedPassword");
        when(userRepository.save(any())).thenReturn(defaultBlogUser);

        ResponseEntity<UserResponse> response = userController.editMe(userToEdit);

        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");
        // response related assertions
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        UserResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(body.getEmail(), oldEmail);
        assertEquals(body.getFirstName(), oldFirstName);
        assertEquals(body.getLastName(), oldLastName);
        assertEquals(body.getAge(), oldAge);
    }

    @Test
    public void testEditUserWhenBlankFirstNameLastNameAndInvalidEmail() {
        String oldLastName = defaultBlogUser.getLastName();
        String oldFirstName = defaultBlogUser.getFirstName();
        String oldEmail = defaultBlogUser.getEmail();
        int oldAge = defaultBlogUser.getAge();

        BlogUser userToEdit = new BlogUser();
        userToEdit.setFirstName("");
        userToEdit.setLastName("");
        userToEdit.setAge(RegisterRequest.MAXIMUM_AGE);
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("notvalidemail");

        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.of(defaultBlogUser));
        when(passwordEncoder.encode(userToEdit.getPassword())).thenReturn("EncodedPassword");
        when(userRepository.save(any())).thenReturn(defaultBlogUser);

        ResponseEntity<UserResponse> response = userController.editMe(userToEdit);
        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");
        // response related assertions
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        UserResponse body = response.getBody();
        assertNotNull(body);
        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");

        assertEquals(body.getEmail(), oldEmail);
        assertEquals(body.getFirstName(), oldFirstName);
        assertEquals(body.getLastName(), oldLastName);
        assertEquals(body.getAge(), oldAge);
    }

    @Test
    public void testEditUserWhenUserDoesNotExists() {
        BlogUser userToEdit = new BlogUser();
        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.empty());
        assertThrows(ResourceDoesNotExistsException.class, () -> userController.editMe(userToEdit));
    }

}