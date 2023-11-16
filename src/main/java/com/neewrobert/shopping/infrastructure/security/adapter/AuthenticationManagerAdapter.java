package com.neewrobert.shopping.infrastructure.security.adapter;

import com.neewrobert.shopping.domain.service.security.CustomAuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationManagerAdapter implements CustomAuthenticationManager {

    private final AuthenticationProvider authenticationManager;

    public AuthenticationManagerAdapter(AuthenticationProvider authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }


}
