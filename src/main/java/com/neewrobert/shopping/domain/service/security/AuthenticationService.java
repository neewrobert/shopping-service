package com.neewrobert.shopping.domain.service.security;

import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.domain.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    private final CustomAuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthenticationService(CustomAuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public String signUp(User user) {
        var saved = userService.registerNewUser(user);
        return jwtService.generateToken(saved);
    }

    public String signIn(String username, String password) {
        authenticationManager.authenticate(username, password);
        var user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Invalid Credentials"));
        return jwtService.generateToken(user);
    }

}
