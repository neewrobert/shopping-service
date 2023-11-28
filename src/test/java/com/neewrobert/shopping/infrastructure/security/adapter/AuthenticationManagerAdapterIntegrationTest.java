package com.neewrobert.shopping.infrastructure.security.adapter;

import com.neewrobert.shopping.infrastructure.RepositoryTestConfiguration;
import com.neewrobert.shopping.infrastructure.persistence.repository.SpringDataUserRepository;
import com.neewrobert.shopping.infrastructure.persistence.repository.UserRepositoryAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.neewrobert.shopping.utils.CommonMethods.getUser;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthenticationManagerAdapterIntegrationTest extends RepositoryTestConfiguration {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    private UserRepositoryAdapter userRepositoryAdapter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthenticationManagerAdapter authenticationManagerAdapter;

    @BeforeEach
    void setUp() {
        authenticationManagerAdapter = new AuthenticationManagerAdapter(authenticationProvider);
        userRepositoryAdapter = new UserRepositoryAdapter(springDataUserRepository);
    }

    @AfterEach
    void tearDown() {
        springDataUserRepository.deleteAll();
    }

    @Test
    void shouldAuthenticate() {
        var password = "password";
        var user = getUser().withPassword(passwordEncoder.encode(password));
        userRepositoryAdapter.save(user);

        Assertions.assertDoesNotThrow(() -> authenticationManagerAdapter.authenticate(user.email(), password));
    }

    @Test
    void shouldNotAuthenticate_throwsBadCredentials() {
        var password = "password";
        var user = getUser().withPassword(passwordEncoder.encode(password));
        userRepositoryAdapter.save(user);

        assertThrows(BadCredentialsException.class, () -> authenticationManagerAdapter.authenticate(user.email(), "wrongPassword"));
    }
}
