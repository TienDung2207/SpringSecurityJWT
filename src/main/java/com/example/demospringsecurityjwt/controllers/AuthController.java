package com.example.demospringsecurityjwt.controllers;

import com.example.demospringsecurityjwt.dto.request.LoginRequest;
import com.example.demospringsecurityjwt.dto.request.SignupRequest;
import com.example.demospringsecurityjwt.dto.response.MessageResponse;
import com.example.demospringsecurityjwt.dto.response.JwtResponse;
import com.example.demospringsecurityjwt.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        ResponseEntity<MessageResponse> loginResponse = authService.handleLogin(loginRequest);

        return loginResponse;
    }

    @PostMapping("signup")
    public ResponseEntity<MessageResponse> register(@RequestBody SignupRequest signupRequest) {
        ResponseEntity<MessageResponse> signupResponse = authService.handleRegisterUser(signupRequest);

        return signupResponse;
    }

    @GetMapping("home")
    public String abc() {
        return "home";
    }

    @GetMapping("user")
    public String user() {
        return "Đây là page cho người dùng";
    }

    @GetMapping("admin")
    public String admin() {
        return "Admin";
    }

}
