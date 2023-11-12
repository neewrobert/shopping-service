package com.neewrobert.shopping.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email must not be blank")
        String email,
        @NotBlank(message = "Password must not be blank")
        String password,
        String createdAt,
        String updatedAt) {

}
