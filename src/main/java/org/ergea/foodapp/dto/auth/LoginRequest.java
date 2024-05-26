package org.ergea.foodapp.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "must not empty")
    @Email
    private String emailAddress;
    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 8, message = "must between 6-8 Characters")
    private String password;
}