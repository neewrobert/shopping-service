package com.neewrobert.shopping.infrastructure.security.adapter;

import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.service.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceAdapter implements JwtService {

    @Value("${jwt.signing.key.secret}")
    private String jwtSigningSecret;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, User userDetails) {
        var username = extractUsername(token);
        return username.equals(userDetails.email()) && !isTokenExpired(token);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.email())
                .issuedAt(new java.util.Date())
                .claim("authorities", user.roles())
                .expiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSigningSecret.getBytes());
    }


}
