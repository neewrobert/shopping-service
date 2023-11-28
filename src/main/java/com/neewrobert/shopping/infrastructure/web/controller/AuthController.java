package com.neewrobert.shopping.infrastructure.web.controller;

import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.service.security.AuthenticationService;
import com.neewrobert.shopping.infrastructure.web.request.UserLoginRequest;
import com.neewrobert.shopping.infrastructure.web.request.UserRequest;
import com.neewrobert.shopping.infrastructure.web.response.JwtAuthenticationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/auth/signing")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@Valid @RequestBody UserLoginRequest request) {
        var token = authenticationService.signIn(request.email(), request.password());
        return ResponseEntity.ok(new JwtAuthenticationResponse(request.email(), token));
    }

    @GetMapping("/user")
    public ResponseEntity<String> testUser() {
        return ResponseEntity.ok("Hello USER");
    }

    @GetMapping("/admin")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> testAdmin() {

        //TODO: NEEDS TO IMPLEMENT LOGIC TO CREATE ADMIN USER
        return ResponseEntity.ok("Hello ADMIN");
    }
}

