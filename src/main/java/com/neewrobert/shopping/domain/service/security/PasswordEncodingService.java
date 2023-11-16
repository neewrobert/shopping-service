package com.neewrobert.shopping.domain.service.security;

public interface PasswordEncodingService {
    String encode(String rawPassword);
}
