package ru.consist.task3.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;
import ru.consist.task3.repository.WorkoutMemoryRepository;
import ru.consist.task3.repository.specification.Specification;
import ru.consist.task3.repository.specification.WorkoutAllSpecification;
import ru.consist.task3.repository.specification.WorkoutIdSpecification;

import java.time.*;
import java.util.ArrayList;
import java.util.List;


class WorkoutServiceImplTest {

    @Mock
    WorkoutMemoryRepository repository;

    @InjectMocks
    WorkoutServiceImpl workoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    List<Workout> getListWorkoutsForTest() {
        List<Workout> workouts = new ArrayList<>();
        workouts.add(new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 22, 34))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(150)
                .setHealth("Хорошее состояние")
                .build());

        workouts.add(new WorkoutBuilder()
                .setId(2)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Силовая")
                .setDuration(LocalTime.of(0, 55, 40))
                .setKilometers(10)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Прекрасное состояние")
                .build());

        return workouts;
    }

    @Test
    void getWorkoutByIdSuccess() {
        List<Workout> workouts = new ArrayList<>();
        var workout = new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 22, 34))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(150)
                .setHealth("Хорошее состояние")
                .build();
        workouts.add(workout);

        Mockito.when(repository.query(Mockito.any(WorkoutIdSpecification.class))).thenReturn(workouts);

        Assertions.assertThat(workoutService.getWorkoutById(1L)).isEqualTo(workout);
    }

    @Test
    void getWorkoutByIdNullObjectSuccess() {
        List<Workout> workouts = new ArrayList<>();

        Mockito.when(repository.query(Mockito.any(WorkoutIdSpecification.class))).thenReturn(workouts);

        Assertions.assertThat(workoutService.getWorkoutById(1L)).isEqualTo(null);
    }

    @Test
    void getTotalKilometersSuccess() {
        List<Workout> workouts = getListWorkoutsForTest();
        Mockito.when(repository.query(Mockito.any(WorkoutAllSpecification.class))).thenReturn(workouts);

        Assertions.assertThat(workoutService.getTotalKilometers()).isEqualTo(15);
    }

    @Test
    void getTotalTrainingTime() {
        List<Workout> workouts = getListWorkoutsForTest();
        Mockito.when(repository.query(Mockito.any(WorkoutAllSpecification.class))).thenReturn(workouts);

        Assertions.assertThat(workoutService.getTotalTrainingTime()).isEqualTo("01:18:14");
    }

    @Test
    void getAmountBurnedCaloriesSuccess() {
        List<Workout> workouts = getListWorkoutsForTest();
        Mockito.when(repository.query(Mockito.any(WorkoutAllSpecification.class))).thenReturn(workouts);

        Assertions.assertThat(workoutService.getAmountBurnedCalories()).isEqualTo(350);
    }
}