package ru.consist.task3.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.consist.task3.repository.WorkoutJdbcRepository;
import ru.consist.task3.repository.WorkoutMemoryRepository;
import ru.consist.task3.repository.WorkoutSpringJdbcRepository;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class RepositoryConfig {

    @Bean
    @ConditionalOnProperty(prefix = "repository", name = "type", havingValue =  "jdbc")
    public WorkoutJdbcRepository workoutJdbcRepository(DataSource dataSource) {
        return new WorkoutJdbcRepository(dataSource);
    }

    @Bean
    @ConditionalOnProperty(prefix = "repository", name = "type", havingValue =  "spring-jdbc")
    public WorkoutSpringJdbcRepository workoutSpringJdbcRepository(DataSource dataSource) {
        return new WorkoutSpringJdbcRepository(dataSource);
    }

    @Bean
    @ConditionalOnProperty(prefix = "repository", name = "type", havingValue =  "in-memory")
    public WorkoutMemoryRepository workoutMemoryRepository() {
        return new WorkoutMemoryRepository(List.of());
    }
}
