package com.api.rest.Unit;

import com.api.rest.BlogUser;
import com.api.rest.config.JwtAuthenticationFilter;
import com.api.rest.config.JwtService;
import com.api.rest.services.UserService;
import com.api.rest.util.GlobalInfo;
import io.jsonwebtoken.security.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JWTAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Before
    public void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService, userService);
    }

    @Test
    public void testValidJwtTokenandStartsWithBearer() throws ServletException, IOException {
        String bearer_token = "Bearer test_token_1345";
        String userName = "testuser@gmail.com";
        String token = "test_token_1345";

        UserDetails user = new User(userName, "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        BlogUser blogUser = new BlogUser();
        blogUser.setEmail(userName);

        when(request.getHeader("Authorization")).thenReturn(bearer_token);
        when(jwtService.extractUserName(token)).thenReturn(userName);
        when(userDetailsService.loadUserByUsername(userName)).thenReturn(user);
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(userService.getUserByEmail(userName)).thenReturn(Optional.of(blogUser));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        BlogUser final_user = GlobalInfo.getBlogUser();

        verify(filterChain).doFilter(request, response);
        assertNotNull(final_user);
        assertEquals(final_user.getEmail(), blogUser.getUsername());
    }

    @Test
    public void testInvalidValidJwtToken() throws ServletException, IOException {
        final String jwt = "invalid_jwt_token_here";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUserName(jwt)).thenThrow(new SignatureException(""));
        when(response.getWriter()).thenReturn(printWriter);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Verify that the response is set as unauthorized
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response.getWriter()).write("Invalid Credentials");

        // Verify that the filter chain is not invoked
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void testNoJwtToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUserName(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

}
