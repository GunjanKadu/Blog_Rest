package com.api.rest.controllers.AuthController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    public static final int MINIMUM_AGE = 18;
    public static final int MAXIMUM_AGE = 65;

    @NotBlank(message = "FirstName Cannot be Null")
    private String firstName;

    @NotBlank(message = "LastName Cannot be Null")
    private String lastName;

    @Email(message = "Email Not valid")
    @NotNull
    private String email;

    @NotNull(message = "Password Cannot be Null")
    private String password;

    @Min(MINIMUM_AGE)
    @Max(MAXIMUM_AGE)
    private Integer age;
}
