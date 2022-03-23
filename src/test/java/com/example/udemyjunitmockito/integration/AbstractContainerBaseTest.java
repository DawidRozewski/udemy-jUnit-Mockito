package com.example.udemyjunitmockito.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems");

        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySources(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }

}
