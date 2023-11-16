package com.neewrobert.shopping.infrastructure.security.adapter;

import com.neewrobert.shopping.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.neewrobert.shopping.utils.CommonMethods.getUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JwtServiceAdapter.class})
class JwtServiceAdapterTest {

    @Autowired
    private JwtServiceAdapter jwtService;

    @Test
    void shouldGenerateToken() {

        var user = getUser();
        var token = jwtService.generateToken(user);

        assertThat(token).isNotEmpty();
    }

    @Test
    void shouldExtractUsernameFromToken() {

        var user = getUser();
        var token = jwtService.generateToken(user);
        var username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo(user.email());
    }

    @Test
    void shouldValidateToken() {

        var user = getUser();
        var token = jwtService.generateToken(user);
        var isValid = jwtService.isTokenValid(token, user);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldInvalidateToken() {
        var user = getUser();
        var token = jwtService.generateToken(user);
        var isValid = jwtService.isTokenValid(token, new User(null, "Jane Doe", "another", "password", null, null, null));
        assertThat(isValid).isFalse();
    }


}
