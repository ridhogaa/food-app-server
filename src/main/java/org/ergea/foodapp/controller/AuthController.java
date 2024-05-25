package org.ergea.foodapp.controller;

import org.ergea.foodapp.config.EmailTemplate;
import org.ergea.foodapp.dto.auth.*;
import org.ergea.foodapp.dto.base.BaseResponse;
import org.ergea.foodapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.register(request), "Success Register User"));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(request), "Success Login User"));
    }

    @PostMapping("register/google")
    public ResponseEntity<?> registerWithGoogle(@RequestBody RegisterRequest request) {
        authService.register(request);
        Object res = authService.sendEmailOtp(new EmailRequest(request.getEmailAddress()), "Register");
        return ResponseEntity.ok(BaseResponse.success(res, "Success Register by Google"));
    }

    @PostMapping("otp")
    public ResponseEntity<?> sendEmailOtp(@RequestBody EmailRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.sendEmailOtp(req, "Register"), "Success Send OTP"));
    }

    @GetMapping("otp/{token}")
    public ResponseEntity<?> confirmOtp(@PathVariable(value = "token") String tokenOtp) {
        return ResponseEntity.ok(BaseResponse.success(authService.confirmOtp(tokenOtp), "Success Send OTP"));
    }

    @PostMapping("password")
    public ResponseEntity<?> sendEmailForgetPassword(@RequestBody EmailRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.sendEmailOtp(req, "Forget Password"), "Success Send OTP Forget Password"));
    }

    @PostMapping("password/otp")
    public ResponseEntity<?> checkValidOtp(@RequestBody OtpRequest otp) {
        return ResponseEntity.ok(BaseResponse.success(authService.checkOtpValid(otp), "Success Validate OTP"));
    }

    @PutMapping("password")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.resetPassword(request), "Success Reset Password"));
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(authService.getCurrentUser(principal), "Success Get Current User Login"));
    }
}
