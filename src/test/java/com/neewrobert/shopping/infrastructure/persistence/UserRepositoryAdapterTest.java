package com.neewrobert.shopping.infrastructure.persistence;

import com.neewrobert.shopping.infrastructure.RepositoryTestConfiguration;
import com.neewrobert.shopping.infrastructure.persistence.repository.SpringDataUserRepository;
import com.neewrobert.shopping.infrastructure.persistence.repository.UserRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static com.neewrobert.shopping.utils.CommonMethods.getUser;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryAdapterTest extends RepositoryTestConfiguration {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    private UserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void setUp() {
        userRepositoryAdapter = new UserRepositoryAdapter(springDataUserRepository);
    }

    @Test
    void shouldSaveAndRetrieveUser() {

        var user = getUser();
        var savedUser = userRepositoryAdapter.save(user);
        var retrievedUser = userRepositoryAdapter.findByEmail(user.email());

        assertThat(retrievedUser).isPresent();
        assertThat(savedUser.name()).isEqualTo(user.name());
    }

    @Test
    void shouldVerifyUserExistsByEmail() {

        var user = getUser();
        assertFalse(userRepositoryAdapter.existsByEmail(user.email()));
        userRepositoryAdapter.save(user);

        assertThat(userRepositoryAdapter.existsByEmail(user.email())).isTrue();
    }
}