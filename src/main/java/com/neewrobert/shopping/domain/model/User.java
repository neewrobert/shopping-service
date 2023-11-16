package com.neewrobert.shopping.domain.model;


import java.time.LocalDateTime;
import java.util.List;

public record User(

        String id,
        String name,
        String email,
        String password,

        List<Role> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static User build(String name, String email, String password) {

        var defaultRoles = List.of(Role.USER);
        var now = LocalDateTime.now();
        return new User(
                null,
                name,
                email,
                password,
                defaultRoles,
                now,
                now);
    }

    public User updated() {
        return new User(
                this.id,
                this.name,
                this.email,
                this.password,
                this.roles,
                this.createdAt,
                LocalDateTime.now());
    }

    public User withPassword(String password) {
        return new User(
                this.id,
                this.name,
                this.email,
                password,
                this.roles,
                this.createdAt,
                this.updatedAt);
    }

}
