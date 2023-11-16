package com.neewrobert.shopping.utils;

import com.neewrobert.shopping.domain.model.Role;
import com.neewrobert.shopping.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class CommonMethods {

    public static User getUser() {
        return new User(null, "John Doe", "johndoe@example.com", "password", List.of(Role.USER), LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1));
    }
}
