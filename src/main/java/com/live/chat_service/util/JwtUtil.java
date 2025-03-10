package com.live.chat_service.util;

import com.live.chat_service.constant.Constant;
import com.live.chat_service.dto.LoginDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.User;
import com.live.chat_service.serviceimpl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final UserServiceImpl userService;

    public JwtUtil(UserServiceImpl userService) {
        this.userService = userService;
    }
private static final String SECRET_KEY = "coherent";

    private Key getSigningKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        } catch (Exception e) {
            throw new CustomValidationExceptions("Error generating signing key");
        }
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();  // This returns the username (subject) from the token
        } catch (Exception e) {
            throw new CustomValidationExceptions("Token is invalid or expired");
        }
    }

    public String generateToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                //.setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Map<String, Object> validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public Map<String, String> login(LoginDto loginDto) {
        User user = userService.userLogin(loginDto);
        if (user == null) {
            throw new CustomValidationExceptions("Invalid credentials");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUserName());
        claims.put("email", user.getEmailId());
        claims.put("role",user.getRole().getRoleName());
        String accessToken = generateToken(claims, user.getEmailId(), Constant.ACCESS_TOKEN_EXPIRATION);
        Map<String, String> tokens = new HashMap<>();
        tokens.put(Constant.ACCESS_TOKEN, accessToken);
        return tokens;
    }

    public boolean validateTokenId(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername()));
    }

}