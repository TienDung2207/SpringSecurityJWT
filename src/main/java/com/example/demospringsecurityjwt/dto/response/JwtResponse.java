package com.example.demospringsecurityjwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;

    private String typeJwt = "Bearer";

    private long expireIn;
}
