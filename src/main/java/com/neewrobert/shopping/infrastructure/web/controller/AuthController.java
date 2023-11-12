package com.neewrobert.shopping.infrastructure.web.controller;

import com.neewrobert.shopping.domain.service.UserService;
import com.neewrobert.shopping.infrastructure.web.dto.UserDTO;
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

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/signup")
    @Validated
    public ResponseEntity<UserDTO> signUp(@Valid @RequestBody UserDTO userDTO) {

        var createdUser = userService.registerNewUser(userDTO);
        URI userURI = URI.create("/api/users/" + createdUser.email());
        return ResponseEntity.created(userURI).body(createdUser);
    }
}
