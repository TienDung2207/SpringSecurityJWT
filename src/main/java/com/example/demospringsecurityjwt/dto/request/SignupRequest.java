package com.example.demospringsecurityjwt.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequest {
    private String username;

    private String password;

    private String role;
}
