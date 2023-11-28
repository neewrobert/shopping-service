package com.neewrobert.shopping.infrastructure;

import com.neewrobert.shopping.infrastructure.persistence.repository.UserRepositoryAdapter;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class RepositoryTestConfiguration {

    private UserRepositoryAdapter userRepositoryAdapter;

    @Container
    protected static final PostgreSQLContainer POSTGRE_SQL_CONTAINER =
            new PostgreSQLContainer(DockerImageName.parse("postgres:15.0"))
                    .withPassword("secret")
                    .withUsername("myuser")
                    .withDatabaseName("shopping");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }


}
