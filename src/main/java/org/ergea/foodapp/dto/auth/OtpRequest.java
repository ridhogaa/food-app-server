package org.ergea.foodapp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    @NotEmpty(message = "Must not empty")
    private String otp;
}