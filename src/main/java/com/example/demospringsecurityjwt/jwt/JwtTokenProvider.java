package com.example.demospringsecurityjwt.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("@{jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateJwtToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .setPayload()
                .compact();
    }

    public String getUserNameFromJwtToken(String jwtToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);

            log.info("abc: " + Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken));


            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String getJwtTypeFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth)) {
            String jwtType = headerAuth.substring(0, headerAuth.indexOf(" "));
            log.info("type JWT: " + jwtType);

            return jwtType;
        }

        return null;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth)) {
            int beginIndex = this.getJwtTypeFromRequest(request).length();
            String jwtToken = headerAuth.substring(beginIndex + 1);
            log.info("jwtToken" + jwtToken);

            return jwtToken;
        }

        return null;
    }

}
