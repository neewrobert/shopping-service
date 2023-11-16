package com.neewrobert.shopping.domain.service.security;

public interface CustomAuthenticationManager {

    void authenticate(String username, String password);
}
