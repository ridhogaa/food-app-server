package org.ergea.foodapp.service;



import org.ergea.foodapp.dto.auth.*;
import org.ergea.foodapp.entity.User;

import java.security.Principal;

public interface AuthService {

    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    Object sendEmailOtp(EmailRequest request, String subject);

    Object confirmOtp(String otp);

    Object checkOtpValid(OtpRequest otp);

    Object resetPassword(ResetPasswordRequest request);

    User getCurrentUser(Principal principal);
}
