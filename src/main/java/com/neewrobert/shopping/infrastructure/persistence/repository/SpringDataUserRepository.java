package com.neewrobert.shopping.infrastructure.persistence.repository;

import com.neewrobert.shopping.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findById(UUID uuid);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);


}
