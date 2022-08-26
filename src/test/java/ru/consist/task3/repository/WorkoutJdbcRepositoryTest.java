package ru.consist.task3.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.consist.task3.model.Exercise;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;
import ru.consist.task3.repository.config.RepositoryTestConfig;
import ru.consist.task3.repository.specification.WorkoutAllSpecification;
import ru.consist.task3.repository.specification.WorkoutIdSpecification;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@ContextConfiguration(classes = RepositoryTestConfig.class)
class WorkoutJdbcRepositoryTest extends AbstractRepositoryTest {

    private WorkoutJdbcRepository workoutJdbcRepository;

    void createWorkout(Workout object) {
        try (Connection conn = dataSource.getConnection()) {
            String sqlWorkout = "insert into workouts (id, date, name, duration," +
                    " kilometers, description, avr_heart_rate, calories, health)" +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement statement = conn.prepareStatement(sqlWorkout)) {
                statement.setLong(1, object.getId());
                statement.setDate(2, Date.valueOf(object.getDate()));
                statement.setString(3, object.getName());
                statement.setTime(4, Time.valueOf(object.getDuration()));
                statement.setInt(5, object.getKilometers());
                statement.setString(6, object.getDescription());
                statement.setInt(7, object.getAvrHeartRate());
                statement.setInt(8, object.getCalories());
                statement.setString(9, object.getHealth());

                statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sqlExercise = "insert into exercises  (exercise_id, name, num_repetitions, num_sets, workout_id)" +
                    " values(?, ?, ?, ?, ?);";
            List<Exercise> exercises = object.getExercises();
            if (exercises != null && exercises.size() != 0) {
                for (Exercise ex : exercises) {
                    try (PreparedStatement statement = conn.prepareStatement(sqlExercise)) {
                        statement.setLong(1, ex.getId());
                        statement.setString(2, ex.getName());
                        statement.setInt(3, ex.getNumRepetitions());
                        statement.setInt(4, ex.getNumSets());
                        statement.setLong(5, ex.getWorkoutId());

                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void readAllWorkouts() {
        Workout workout = new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2019, 4, 17))
                .setName("Силовая")
                .setDuration(LocalTime.of(0, 30, 15))
                .setKilometers(0)
                .setDescription("Упражнения на пресс")
                .setAvrHeartRate(162)
                .setCalories(178)
                .setHealth("Хорошее самочувствие")
                .build();
        createWorkout(workout);
        workoutJdbcRepository = new WorkoutJdbcRepository(dataSource);
        var readResult = workoutJdbcRepository.query(new WorkoutAllSpecification());
        Assertions.assertThat(readResult).filteredOn(workout1 -> workout1.getId() == 1);
    }

    @Test
    void readWorkoutById() {
        Workout workout = new WorkoutBuilder()
                .setId(3)
                .setDate(LocalDate.of(2020, 2, 21))
                .setName("Силовая")
                .setDuration(LocalTime.of(0, 45, 05))
                .setKilometers(0)
                .setDescription("Упражнения на пресс")
                .setAvrHeartRate(165)
                .setCalories(178)
                .setHealth("Хорошее самочувствие")
                .build();
        createWorkout(workout);
        workoutJdbcRepository = new WorkoutJdbcRepository(dataSource);
        var readResult = workoutJdbcRepository.query(new WorkoutIdSpecification(3L));

        Assertions.assertThat(readResult.get(0))
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "Силовая")
                .hasFieldOrPropertyWithValue("kilometers", 0)
                .hasFieldOrPropertyWithValue("description", "Упражнения на пресс")
                .hasFieldOrPropertyWithValue("avrHeartRate", 165);
    }

    @Test
    void createWorkout() {
        Workout workout = new WorkoutBuilder()
                .setId(5)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();
        workout.setExercises(List.of(new Exercise(
                1,
                "Отжимания",
                10,
                3,
                5
        )));
        createWorkout(workout);

        var resultWorkout = jdbcTemplate
                .queryForMap("select * from workouts where id = :workoutId", Map.of("workoutId", 5L));

        Assertions.assertThat(resultWorkout)
                .hasFieldOrPropertyWithValue("id", 5L)
                .hasFieldOrPropertyWithValue("name", "Кросс")
                .hasFieldOrPropertyWithValue("kilometers", 5)
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("avr_heart_rate", 170);


        var resultExercise = jdbcTemplate
                .queryForMap("select * from exercises where exercise_id = :exerciseId", Map.of("exerciseId", 1L));

        Assertions.assertThat(resultExercise)
                .hasFieldOrPropertyWithValue("exercise_id", 1L)
                .hasFieldOrPropertyWithValue("name", "Отжимания")
                .hasFieldOrPropertyWithValue("num_repetitions", 10)
                .hasFieldOrPropertyWithValue("num_sets", 3)
                .hasFieldOrPropertyWithValue("workout_id", 5L);
    }

    @Test
    void updateWorkoutById() {
        Workout workout = new WorkoutBuilder()
                .setId(6)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 48, 0))
                .setKilometers(5)
                .setDescription("Интервальная тренировка")
                .setAvrHeartRate(180)
                .setCalories(200)
                .setHealth("Хорошее самочувствие")
                .build();
        workout.setExercises(List.of(new Exercise(
                2,
                "Скручивания",
                20,
                2,
                6
        )));
        createWorkout(workout);
        workoutJdbcRepository = new WorkoutJdbcRepository(dataSource);
        workoutJdbcRepository.update(workout);

        var resultWorkout = jdbcTemplate
                .queryForMap("select * from workouts where id = :workoutId", Map.of("workoutId", 6L));

        Assertions.assertThat(resultWorkout)
                .hasFieldOrPropertyWithValue("id", 6L)
                .hasFieldOrPropertyWithValue("name", "Кросс")
                .hasFieldOrPropertyWithValue("kilometers", 5)
                .hasFieldOrPropertyWithValue("description", "Интервальная тренировка")
                .hasFieldOrPropertyWithValue("avr_heart_rate", 180);


        var resultExercise = jdbcTemplate
                .queryForMap("select * from exercises where exercise_id = :exerciseId", Map.of("exerciseId", 2L));

        Assertions.assertThat(resultExercise)
                .hasFieldOrPropertyWithValue("exercise_id", 2L)
                .hasFieldOrPropertyWithValue("name", "Скручивания")
                .hasFieldOrPropertyWithValue("num_repetitions", 20)
                .hasFieldOrPropertyWithValue("num_sets", 2)
                .hasFieldOrPropertyWithValue("workout_id", 6L);
    }

    @Test
    void deleteWorkoutById() {
        Workout workout = new WorkoutBuilder()
                .setId(7)
                .setDate(LocalDate.of(2022, 1, 12))
                .setName("Лыжная подготовка")
                .setDuration(LocalTime.of(0, 30, 5))
                .setKilometers(15)
                .setDescription("Тренировка классическим стилем")
                .setAvrHeartRate(165)
                .setCalories(205)
                .setHealth("Хорошее самочувствие")
                .build();
        workout.setExercises(List.of(new Exercise(
                3,
                "Велосипед",
                15,
                3,
                7
        )));
        createWorkout(workout);
        workoutJdbcRepository = new WorkoutJdbcRepository(dataSource);
        workoutJdbcRepository.delete(workout);
        Assertions.assertThat(jdbcTemplate.queryForList("select * from workouts where id = :workoutId",
                Map.of("workoutId", 7L))).isEmpty();

        Assertions.assertThat(jdbcTemplate.queryForList("select * from exercises where exercise_id = :exerciseId",
                Map.of("exerciseId", 3L))).isEmpty();
    }
}