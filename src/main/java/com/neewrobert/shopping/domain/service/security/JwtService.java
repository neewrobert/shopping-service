package com.neewrobert.shopping.domain.service.security;

import com.neewrobert.shopping.domain.model.User;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token, User user);

    String generateToken(User user);
}
