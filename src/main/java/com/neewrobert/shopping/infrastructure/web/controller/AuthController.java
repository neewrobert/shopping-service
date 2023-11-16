package com.neewrobert.shopping.infrastructure.web.controller;

import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.service.security.AuthenticationService;
import com.neewrobert.shopping.infrastructure.web.request.UserRequest;
import com.neewrobert.shopping.infrastructure.web.response.JwtAuthenticationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController()
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth/signup")
    @Validated
    public ResponseEntity<JwtAuthenticationResponse> signUp(@Valid @RequestBody UserRequest request) {
        var token = authenticationService.signUp(User.build(request.name(), request.email(), request.password()));
        var userURI = URI.create("/api/users/" + request.email());
        return ResponseEntity.created(userURI).body(new JwtAuthenticationResponse(request.email(), token));
    }

}

