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
        //String refreshToken = generateToken(claims, user.getEmailId(), Constant.REFRESH_TOKEN_EXPIRATION);
        Map<String, String> tokens = new HashMap<>();
        tokens.put(Constant.ACCESS_TOKEN, accessToken);
        //tokens.put(Constant.REFRESH_TOKEN, refreshToken);

        return tokens;
    }

    public Map<String, String> refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomValidationExceptions("Refresh token is required");
        }
        Map<String, Object> claims = validateToken(refreshToken);
        if (claims == null) {
            throw new CustomValidationExceptions("Invalid or expired refresh token");
        }
        String newAccessToken = generateToken(claims, (String) claims.get("email"), Constant.ACCESS_TOKEN_EXPIRATION);
        String newRefreshToken = generateToken(claims, (String) claims.get("email"), Constant.REFRESH_TOKEN_EXPIRATION);
        return Map.of(Constant.ACCESS_TOKEN, newAccessToken,Constant.REFRESH_TOKEN,newRefreshToken);
    }

    public boolean validateTokenId(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new CustomValidationExceptions("Error extracting claims from token");
        }
    }
}