package org.ergea.foodapp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = "Must not empty")
    @Email
    private String emailAddress;
    @NotEmpty(message = "Must not empty")
    private String otp;
    @NotEmpty(message = "Must not empty")
    @Size(min = 6, max = 8, message = "must between 6-8 Characters")
    private String newPassword;
}