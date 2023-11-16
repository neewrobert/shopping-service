package com.neewrobert.shopping.domain.service;

import com.neewrobert.shopping.domain.exception.UserAlreadyExistsException;
import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.domain.service.security.PasswordEncodingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.neewrobert.shopping.utils.CommonMethods.getUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserService.class})
class UserServiceTest {


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncodingService passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    void shouldRegisterNewUser() {

        when(userRepository.save(any())).thenReturn(getUser());
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        var user = getUser();
        var registeredUser = userService.registerNewUser(user);
        Assertions.assertEquals(registeredUser.email(), user.email());

    }

    @Test
    void shouldThrowUserAlreadyExistsException() {

        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
        var user = getUser();
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerNewUser(user));
    }

}
