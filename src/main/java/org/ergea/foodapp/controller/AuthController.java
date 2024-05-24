package org.ergea.foodapp.controller;

import org.apache.coyote.Response;
import org.ergea.foodapp.dto.BaseResponse;
import org.ergea.foodapp.dto.auth.LoginRequest;
import org.ergea.foodapp.dto.auth.RegisterRequest;
import org.ergea.foodapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
