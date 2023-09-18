package com.api.rest.controllers.UserController;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserEditRequest {

    @NotBlank(message = "FirstName Cannot be Null")
    private String firstName;

    @NotBlank(message = "LastName Cannot be Null")
    private String lastName;

    @Email(message = "Email Not valid")
    @NotNull
    private String email;

    @NotNull(message = "Password Cannot be Null")
    private String password;

    @Min(18)
    @Max(65)
    private Integer age;
}
