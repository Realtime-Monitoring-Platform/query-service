package com.realtime_monitoring.query_service.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    // @Value("${jwt.secret}")
    // private String secret;

    // @Value("${jwt.expiration}")
    // private long expiration;

    @Value("${spring.application.security.jwt.secret-key}")
    private String secret;

    @Value("${spring.application.security.jwt.expiration}")
    private long expiration;

    @Value("${spring.application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        DecodedJWT decoded = JWT.decode(token);
        return decoded.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);
            
            Date expiresAt = decoded.getExpiresAt();
            if (expiresAt == null) {
                return false;
            }
            return expiresAt.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}