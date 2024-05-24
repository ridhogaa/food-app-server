package org.ergea.foodapp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 8, message = "must between 6-8 Characters")
    private String username;
    @Email
    private String emailAddress;
    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 8, message = "must between 6-8 Characters")
    private String password;
}
