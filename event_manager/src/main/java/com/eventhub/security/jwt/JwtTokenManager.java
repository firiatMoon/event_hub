package com.eventhub.security.jwt;

import com.eventhub.dto.JwtTokenResponse;
import com.eventhub.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenManager {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenManager.class);

    @Value("${token.jwt-token-key}")
    private String jwtSecret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public JwtTokenResponse generateAuthToken(String login, Long userId, Role role) {
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
        jwtTokenResponse.setJwtToken(generateToken(login, userId, role));
        jwtTokenResponse.setRefreshToken(generateRefreshToken(login));
        return jwtTokenResponse;
    }

    public JwtTokenResponse refreshBaseToken(String login, String refreshToken, Long userId, Role role) {
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
        jwtTokenResponse.setJwtToken(generateToken(login, userId, role));
        jwtTokenResponse.setRefreshToken(refreshToken);
        return jwtTokenResponse;
    }

    private String generateToken(String login, Long userId, Role role) {
        Date date = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(login)
                .claim("userId", userId)
                .claim("role", role)
                .signWith(secretKey)
                .expiration(date)
                .compact();
    }

    private String generateRefreshToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(login)
                .signWith(secretKey)
                .expiration(date)
                .compact();
    }

    public String getLoginFromToken(String jwtToken){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JwtException: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JwtException: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed JwtException: {}", e.getMessage());
        } catch (SecurityException e) {
            log.error("SecurityException: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
        }
        return false;
    }
}
