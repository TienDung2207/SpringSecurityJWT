package com.example.demospringsecurityjwt.security;

import com.example.demospringsecurityjwt.jwt.AuthEntryPointJwt;
import com.example.demospringsecurityjwt.jwt.JwtAuthFilter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SpringSecurity {

    private final AuthEntryPointJwt authEntryPointJwt;

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJwt).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/home", "/login", "/signup")
                .permitAll()
                .antMatchers("/admin")
                .hasAuthority("admin")
                .antMatchers("/user")
                .hasAnyAuthority("user", "admin")
                .anyRequest().authenticated().and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
