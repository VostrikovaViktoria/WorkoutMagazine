package ru.consist.task3.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;
import ru.consist.task3.repository.config.RepositoryTestConfig;
import ru.consist.task3.repository.specification.WorkoutAllSpecification;
import ru.consist.task3.repository.specification.WorkoutIdSpecification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@ContextConfiguration(classes = RepositoryTestConfig.class)
class WorkoutSpringJdbcRepositoryTest extends AbstractRepositoryTest {

    private WorkoutSpringJdbcRepository databaseRepository;

    @Test
    @Sql(statements = {"insert into workouts (id, date, name, duration, kilometers, description, avr_heart_rate, calories, health)" +
            "values(1, '2019-04-17', 'Силовая', '00:30:15', 0, 'Упражнения на пресс', 162, 178, 'Хорошее самочувствие')," +
            "(2, '2019-05-12', 'Кросс', '00:47:00', 7, 'Интервальная тренировка', 180, 200, 'Прекрасное самочувствие');"})
    void readAllWorkouts() {
        databaseRepository = new WorkoutSpringJdbcRepository(dataSource);
        var readResult = databaseRepository.query(new WorkoutAllSpecification());

        Assertions.assertThat(readResult.get(0))
                .hasFieldOrPropertyWithValue("name", "Силовая")
                .hasFieldOrPropertyWithValue("kilometers", 0)
                .hasFieldOrPropertyWithValue("description", "Упражнения на пресс")
                .hasFieldOrPropertyWithValue("avrHeartRate", 162);
    }

    @Test
    @Sql(statements = {"insert into workouts (id, date, name, duration, kilometers, description, avr_heart_rate, calories, health)" +
            "values(2, '2020-02-21', 'Силовая', '00:45:05', 0, 'Упражнения на пресс', 165, 178, 'Хорошее самочувствие')," +
            "(3, '2020-06-12', 'Кросс', '00:20:00', 3, 'Бег', 150, 200, 'Прекрасное самочувствие');"})
    void readWorkoutById() {
        databaseRepository = new WorkoutSpringJdbcRepository(dataSource);
        var readResult = databaseRepository.query(new WorkoutIdSpecification(2L));

        Assertions.assertThat(readResult.get(0))
                .hasFieldOrPropertyWithValue("name", "Силовая")
                .hasFieldOrPropertyWithValue("kilometers", 0)
                .hasFieldOrPropertyWithValue("description", "Упражнения на пресс")
                .hasFieldOrPropertyWithValue("avrHeartRate", 165);
    }

    @Test
    void createWorkout() {
        Workout workout = new WorkoutBuilder()
                .setId(3)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();
        databaseRepository = new WorkoutSpringJdbcRepository(dataSource);
        databaseRepository.create(workout);

        var result = jdbcTemplate
                .queryForMap("select * from workouts where id = :workoutId", Map.of("workoutId", 3L));

        Assertions.assertThat(result)
                .hasFieldOrPropertyWithValue("name", "Кросс")
                .hasFieldOrPropertyWithValue("kilometers", 5)
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("avr_heart_rate", 170);
    }

    @Test
    @Sql(statements = {"insert into workouts (id, date, name, duration, kilometers, description, avr_heart_rate, calories, health)" +
            "values(4, '2022-05-21', 'Кросс', '00:48:00', 7, 'Интервальная тренировка', 180, 200, 'Хорошее самочувствие');"})
    void updateWorkoutById() {
        Workout workout = new WorkoutBuilder()
                .setId(4)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 48, 0))
                .setKilometers(5)
                .setDescription("Интервальная тренировка")
                .setAvrHeartRate(180)
                .setCalories(200)
                .setHealth("Хорошее самочувствие")
                .build();
        databaseRepository = new WorkoutSpringJdbcRepository(dataSource);
        databaseRepository.update(workout);

        var result = jdbcTemplate
                .query("select * from workouts where id = :workoutId", Map.of("workoutId", 4L),
                        (rs, rowNum) -> new WorkoutBuilder()
                                        .setId(rs.getLong("id"))
                                        .setDate(rs.getDate("date").toLocalDate())
                                        .setName(rs.getString("name"))
                                        .setDuration(rs.getTime("duration").toLocalTime())
                                        .setKilometers(rs.getInt("kilometers"))
                                        .setDescription(rs.getString("description"))
                                        .setAvrHeartRate(rs.getInt("avr_heart_rate"))
                                        .setCalories(rs.getInt("calories"))
                                        .setHealth(rs.getString("health"))
                                        .build());

        Assertions.assertThat(result.get(0))
                .hasFieldOrPropertyWithValue("name", "Кросс")
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 4, 5))
                .hasFieldOrPropertyWithValue("kilometers", 5);
    }

    @Test
    @Sql(statements = {"insert into workouts (id, date, name, duration, kilometers, description, avr_heart_rate, calories, health)" +
            "values(5, '2022-01-12', 'Лыжная подготовка', '00:30:05', 15, " +
            "'Тренировка классическим стилем', 165, 205, 'Хорошее самочувствие');"})
    void deleteWorkoutById() {
        Workout workout = new WorkoutBuilder()
                .setId(5)
                .setDate(LocalDate.of(2022, 1, 12))
                .setName("Лыжная подготовка")
                .setDuration(LocalTime.of(0, 30, 5))
                .setKilometers(15)
                .setDescription("Тренировка классическим стилем")
                .setAvrHeartRate(165)
                .setCalories(205)
                .setHealth("Хорошее самочувствие")
                .build();
        databaseRepository = new WorkoutSpringJdbcRepository(dataSource);
        databaseRepository.delete(workout);

        Assertions.assertThat(jdbcTemplate.queryForList("select * from workouts where id = :workoutId",
                Map.of("workoutId", 5L))).isEmpty();
    }
}