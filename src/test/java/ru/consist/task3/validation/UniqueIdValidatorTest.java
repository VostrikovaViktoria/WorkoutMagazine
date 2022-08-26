package ru.consist.task3.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;
import ru.consist.task3.repository.WorkoutMemoryRepository;
import ru.consist.task3.service.WorkoutService;
import ru.consist.task3.service.WorkoutServiceImpl;

import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UniqueIdValidatorTest {

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Mock
    WorkoutService workoutService;

    @InjectMocks
    UniqueIdValidator uniqueIdValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void idWorkoutIsValid() {
        List<Workout> workoutList = List.of(
                new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build(),
                new WorkoutBuilder()
                .setId(2)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Силовая")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(0)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Прекрасное состояние")
                .build());

        Mockito.when(workoutService.getAllWorkouts()).thenReturn(workoutList);

        Assertions.assertThat(uniqueIdValidator.isValid(5L, constraintValidatorContext)).isTrue();
    }

    @Test
    void idWorkoutIsNotValid() {
        List<Workout> workoutList = List.of(
                new WorkoutBuilder()
                        .setId(3)
                        .setDate(LocalDate.of(2022, 4, 5))
                        .setName("Кросс")
                        .setDuration(LocalTime.of(0, 30, 13))
                        .setKilometers(5)
                        .setDescription("description")
                        .setAvrHeartRate(170)
                        .setCalories(200)
                        .setHealth("Хорошее состояние")
                        .build(),
                new WorkoutBuilder()
                        .setId(4)
                        .setDate(LocalDate.of(2022, 4, 5))
                        .setName("Силовая")
                        .setDuration(LocalTime.of(0, 30, 13))
                        .setKilometers(0)
                        .setDescription("description")
                        .setAvrHeartRate(170)
                        .setCalories(200)
                        .setHealth("Прекрасное состояние")
                        .build());

        Mockito.when(workoutService.getAllWorkouts()).thenReturn(workoutList);

        Assertions.assertThat(uniqueIdValidator.isValid(3L, constraintValidatorContext)).isFalse();
    }
}