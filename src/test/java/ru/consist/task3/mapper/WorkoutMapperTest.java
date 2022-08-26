package ru.consist.task3.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.consist.task3.dto.WorkoutDto;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutMapperTest {

    @Test
    void workoutToWorkoutDtoSuccess() {
        Workout workout = new WorkoutBuilder()
                .setId(2)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        WorkoutDto workoutDtoExpected = new WorkoutDto(
                2,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );

        WorkoutDto workoutDtoActual = new WorkoutMapper().workoutToWorkoutDto(workout);

        Assertions.assertThat(workoutDtoActual).usingRecursiveComparison().isEqualTo(workoutDtoExpected);
    }

    @Test
    void workoutDtoToWorkoutSuccess() {
        Workout workoutExpected = new WorkoutBuilder()
                .setId(2)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        WorkoutDto workoutDto = new WorkoutDto(
                2,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );

        Workout workoutActual = new WorkoutMapper().workoutDtoToWorkout(workoutDto);

        Assertions.assertThat(workoutActual).usingRecursiveComparison().isEqualTo(workoutExpected);
    }
}