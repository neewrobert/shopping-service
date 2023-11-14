package com.neewrobert.shopping.infrastructure.persistence;

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
import static utils.CommonMethods.getUser;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryAdapterTest {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    private UserRepositoryAdapter userRepositoryAdapter;

    @Container
    static PostgreSQLContainer database = new PostgreSQLContainer<>("postgres:15.0")
            .withDatabaseName("shopping")
            .withUsername("myuser")
            .withPassword("secret");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

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