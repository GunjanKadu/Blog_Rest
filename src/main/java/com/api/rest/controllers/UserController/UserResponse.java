package com.api.rest.controllers.UserController;

import com.api.rest.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private Role role;
}
