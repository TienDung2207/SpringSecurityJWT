package com.example.demospringsecurityjwt.jwt;

import com.example.demospringsecurityjwt.entity.User;
import com.example.demospringsecurityjwt.repositories.UserRepository;
import com.example.demospringsecurityjwt.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
            log.info("authentication: " + authentication2);
            String jwtToken = jwtTokenProvider.getJwtFromRequest(request);
            if (jwtToken != null && jwtTokenProvider.validateJwtToken(jwtToken)) {
                String userName = jwtTokenProvider.getUserNameFromJwtToken(jwtToken);

                User user = authService.getByUserName(userName);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getUserName(), user.getHashedPassword(), Arrays.asList(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.info("DSSSSS");
            log.info("DAY S");
            log.error("Fail on set user authentication: {}", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

}
