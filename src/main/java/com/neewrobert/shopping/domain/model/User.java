package com.neewrobert.shopping.domain.model;


import java.time.LocalDateTime;

public record User(
        String name,
        String email,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}