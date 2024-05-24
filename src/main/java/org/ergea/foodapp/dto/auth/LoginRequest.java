package org.ergea.foodapp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 8, message = "must between 6-8 Characters")
    private String username;
    @NotEmpty(message = "must not empty")
    @Size(min = 6, max = 8, message = "must between 6-8 Characters")
    private String password;
}
