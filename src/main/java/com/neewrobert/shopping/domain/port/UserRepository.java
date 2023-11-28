package com.neewrobert.shopping.domain.port;

import com.neewrobert.shopping.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteAll();
}
