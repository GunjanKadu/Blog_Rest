package com.api.rest.Unit.Services;

import com.api.rest.BlogUser;
import com.api.rest.Role;
import com.api.rest.controllers.AuthController.RegisterRequest;
import com.api.rest.controllers.UserController.UserResponse;
import com.api.rest.exceptions.ResourceDoesNotExistsException;
import com.api.rest.repositories.UserRepository;
import com.api.rest.services.UserService;
import com.api.rest.util.GlobalInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
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
    public void testEditUserWithCorrectInput() {
        BlogUser userToEdit = new BlogUser();
        userToEdit.setFirstName("EditedFirstName");
        userToEdit.setLastName("EditedLastName");
        userToEdit.setAge(50);
        userToEdit.setPassword("editedPassword");
        userToEdit.setEmail("editedEmail@gmail.com");

        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.of(defaultBlogUser));
        when(passwordEncoder.encode(userToEdit.getPassword())).thenReturn("EncodedPassword");
        when(userRepository.save(any())).thenReturn(defaultBlogUser);

        UserResponse userEditResponse = userService.editUser(userToEdit);

        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");

        assertEquals(userEditResponse.getEmail(), userToEdit.getEmail());
        assertEquals(userEditResponse.getFirstName(), userToEdit.getFirstName());
        assertEquals(userEditResponse.getLastName(), userToEdit.getLastName());
        assertEquals(userEditResponse.getAge(), userToEdit.getAge());
    }

    @Test
    public void testEditUserWhenFirstNameLastNameAndEmailAreNotPresent() {
        String oldLastName = defaultBlogUser.getLastName();
        String oldFirstName = defaultBlogUser.getFirstName();
        String oldEmail = defaultBlogUser.getEmail();

        BlogUser userToEdit = new BlogUser();
        userToEdit.setAge(50);
        userToEdit.setPassword("editedPassword");

        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.of(defaultBlogUser));
        when(passwordEncoder.encode(userToEdit.getPassword())).thenReturn("EncodedPassword");
        when(userRepository.save(any())).thenReturn(defaultBlogUser);

        UserResponse userEditResponse = userService.editUser(userToEdit);

        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");

        assertEquals(userEditResponse.getEmail(), oldEmail);
        assertEquals(userEditResponse.getFirstName(), oldFirstName);
        assertEquals(userEditResponse.getLastName(), oldLastName);
        assertEquals(userEditResponse.getAge(), userToEdit.getAge());
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

        UserResponse userEditResponse = userService.editUser(userToEdit);

        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");

        assertEquals(userEditResponse.getEmail(), oldEmail);
        assertEquals(userEditResponse.getFirstName(), oldFirstName);
        assertEquals(userEditResponse.getLastName(), oldLastName);
        assertEquals(userEditResponse.getAge(), oldAge);
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

        UserResponse userEditResponse = userService.editUser(userToEdit);

        assertNotEquals(userToEdit.getPassword(), defaultBlogUser.getPassword());
        assertEquals(defaultBlogUser.getPassword(), "EncodedPassword");

        assertEquals(userEditResponse.getEmail(), oldEmail);
        assertEquals(userEditResponse.getFirstName(), oldFirstName);
        assertEquals(userEditResponse.getLastName(), oldLastName);
        assertEquals(userEditResponse.getAge(), oldAge);
    }

    @Test
    public void testEditUserWhenUserDoesNotExists() {
        BlogUser userToEdit = new BlogUser();
        when(userRepository.findByEmail(defaultBlogUser.getEmail())).thenReturn(Optional.empty());
        assertThrows(ResourceDoesNotExistsException.class, () -> userService.editUser(userToEdit));
    }
}
