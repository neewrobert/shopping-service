package com.neewrobert.shopping.infrastructure.persistence.repository;

import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;

    public UserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity saved = springDataUserRepository.save(toUserEntity(user));
        return toUser(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> optionalUserEntity = springDataUserRepository.findById(id);
        return optionalUserEntity.map(this::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> optionalUserEntity = springDataUserRepository.findByEmail(email);
        return optionalUserEntity.map(this::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

    private UserEntity toUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.name());
        userEntity.setEmail(user.email());
        userEntity.setPassword(user.password());
        userEntity.setCreatedAt(user.createdAt());
        userEntity.setUpdatedAt(user.updatedAt());

        return userEntity;
    }

    private User toUser(UserEntity userEntity) {
        return new User(
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt());
    }
}
