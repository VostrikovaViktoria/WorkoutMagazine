package ru.consist.task3.repository;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractDatabaseTest {
    private static final String POSTGRES_DOCKER_VERSION = "postgres:14";

    protected static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(POSTGRES_DOCKER_VERSION)
            .withDatabaseName("test_workouts");

    static {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url",() -> container.getJdbcUrl() + "&stringtype=unspecified");
        propertyRegistry.add("spring.datasource.username", () -> container.getUsername());
        propertyRegistry.add("spring.datasource.password", () -> container.getPassword());
    }
}
