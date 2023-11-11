package com.neewrobert.shopping.domain.service;

import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.infrastructure.web.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public UserDTO registerNewUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new RuntimeException("User already exists");
        }
        var user = toUser(userDTO);
        return toUserDTO(userRepository.save(user));
    }

    private User toUser(UserDTO userDTO) {
        return new User(
                userDTO.name(),
                userDTO.email(),
                passwordEncoder.encode(userDTO.password()),
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    private UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.name(),
                user.email(),
                null,
                user.createdAt().toString(),
                user.updatedAt().toString());
    }
}
