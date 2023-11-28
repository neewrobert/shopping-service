package com.neewrobert.shopping.domain.service.security;

import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.domain.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.neewrobert.shopping.utils.CommonMethods.getUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private CustomAuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldSignUp() {

        when(userService.registerNewUser(any())).thenReturn(getUser());
        when(jwtService.generateToken(any())).thenReturn("token");

        var user = getUser();
        var token = authenticationService.signUp(user);
        assertThat(token).isNotNull();
    }

    @Test
    void shouldThrowUserAlreadyExistsException() {

        when(userService.registerNewUser(any())).thenThrow(new IllegalArgumentException("Email already exists:" + getUser().email()));

        var user = getUser();
        Assertions.assertThatThrownBy(() -> authenticationService.signUp(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists:" + user.email());
    }

    @Test
    void shouldSignIn() {
        var user = getUser();
        doNothing().when(authenticationManager).authenticate(any(), any());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("token");

        var token = authenticationService.signIn(user.email(), user.password());
        assertThat(token).isNotNull();
    }

    @Test
    void shouldThrowIllegalArgumentException_InvalidCredentials() {
        var user = getUser();
        doNothing().when(authenticationManager).authenticate(any(), any());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.signIn(user.email(), user.password()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid Credentials");
    }

}
