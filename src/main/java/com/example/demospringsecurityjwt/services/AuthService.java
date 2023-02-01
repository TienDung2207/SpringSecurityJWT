package com.example.demospringsecurityjwt.services;

import com.example.demospringsecurityjwt.configs.Config;
import com.example.demospringsecurityjwt.dto.request.LoginRequest;
import com.example.demospringsecurityjwt.dto.request.SignupRequest;
import com.example.demospringsecurityjwt.dto.response.JwtResponse;
import com.example.demospringsecurityjwt.dto.response.MessageResponse;
import com.example.demospringsecurityjwt.entity.User;
import com.example.demospringsecurityjwt.jwt.JwtTokenProvider;
import com.example.demospringsecurityjwt.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    @Value("${jwt.expiration}")
    private long expiration;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final Config config;
    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, Config config) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.config = config;
    }

    public ResponseEntity<MessageResponse> handleLogin(LoginRequest loginRequest) {
        String userName = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.getByUserName(userName);

        if (user != null) {
            String hashKey = user.getHashKey();
            String hashedPassInDB = user.getHashedPassword();
            String hashedPassword = config.hmacSHA512(hashKey, config.md5(password));

            if (hashedPassInDB.equals(hashedPassword)) {
                String jwtToken = jwtTokenProvider.generateJwtToken(userName);
                JwtResponse jwt = new JwtResponse();
                jwt.setAccessToken(jwtToken);
                jwt.setExpireIn(expiration / 1000);

                MessageResponse loginResponse = new MessageResponse("00", "Signed in successfully!", jwt);
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            } else {
                log.error("Invalid credentials");
                return ResponseEntity.badRequest().body(new MessageResponse("400", "Invalid credentials!"));
            }
        } else {
            log.error("Could not find user: {}", userName);
            return ResponseEntity.badRequest().body(new MessageResponse("400", "Could not find user!"));
        }
    }

    public ResponseEntity<MessageResponse> handleRegisterUser(SignupRequest signupRequest) {
        try {
            if (userRepository.existsByUserName(signupRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("400", "Username is already taken!"));
            }
            String hashKey = config.randomKey(32);
            String hashedPassword = config.hmacSHA512(hashKey, config.md5(signupRequest.getPassword()));

            User user = new User();
            user.setUserName(signupRequest.getUsername());
            user.setHashKey(hashKey);
            user.setRole(signupRequest.getRole());
            user.setHashedPassword(hashedPassword);
            userRepository.save(user);

            return ResponseEntity.ok().body(new MessageResponse("00", "User registered successfully!"));
        } catch (Exception ex) {
            log.error("ex register: " + ex.getMessage());
            return ResponseEntity.internalServerError().body(new MessageResponse("500", "Internal Server Error!"));
        }
    }

    public User getByUserName(String userName) {
        return userRepository.getByUserName(userName);
    }
}
