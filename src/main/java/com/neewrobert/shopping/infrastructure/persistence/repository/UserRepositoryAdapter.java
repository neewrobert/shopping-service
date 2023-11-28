package com.neewrobert.shopping.infrastructure.persistence.repository;

import com.neewrobert.shopping.domain.model.Role;
import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
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
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> optionalUserEntity = springDataUserRepository.findByEmail(email);
        return optionalUserEntity.map(this::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteAll() {
        springDataUserRepository.deleteAll();
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
                userEntity.getId().toString(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                List.of(Role.USER),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt());
    }
}
