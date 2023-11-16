package com.neewrobert.shopping.domain.service;

import com.neewrobert.shopping.domain.exception.UserAlreadyExistsException;
import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.domain.service.security.PasswordEncodingService;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final PasswordEncodingService passwordEncoding;
    private final UserRepository userRepository;

    public UserService(PasswordEncodingService passwordEncoder, UserRepository userRepository) {
        this.passwordEncoding = passwordEncoder;
        this.userRepository = userRepository;

    }


    public User registerNewUser(User user) {
        if (userRepository.existsByEmail(user.email())) {
            throw new UserAlreadyExistsException("Email already exists:" + user.email());
        }
        return userRepository.save(user.withPassword(passwordEncoding.encode(user.password())));
    }



}
