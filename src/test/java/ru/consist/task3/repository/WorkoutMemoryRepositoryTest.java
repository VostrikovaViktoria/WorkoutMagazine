package ru.consist.task3.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;
import ru.consist.task3.repository.specification.WorkoutAllSpecification;
import ru.consist.task3.repository.specification.WorkoutIdSpecification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class WorkoutMemoryRepositoryTest {

    private Workout workout1 =  new WorkoutBuilder()
            .setId(1)
            .setDate(LocalDate.of(2022, 4, 5))
            .setName("Кросс")
            .setDuration(LocalTime.of(0, 30, 13))
            .setKilometers(5)
            .setDescription("description")
            .setAvrHeartRate(170)
            .setCalories(200)
            .setHealth("Хорошее состояние")
            .build();

    private Workout workout2 = new WorkoutBuilder()
            .setId(2)
            .setDate(LocalDate.of(2022, 4, 5))
            .setName("Силовая")
            .setDuration(LocalTime.of(0, 30, 13))
            .setKilometers(0)
            .setDescription("description")
            .setAvrHeartRate(170)
            .setCalories(200)
            .setHealth("Прекрасное состояние")
            .build();

    @Test
    void createWorkoutSuccess() {
        WorkoutMemoryRepository repository = new WorkoutMemoryRepository(new ArrayList<>());

        boolean actualValue = repository.create(workout1);
        Assertions.assertThat(actualValue).isEqualTo(true);
    }

    @Test
    void falseWhenCreateWorkoutByExistingId() {
        WorkoutMemoryRepository repository =
                new WorkoutMemoryRepository(new ArrayList<>(List.of(workout1, workout2)));

        boolean actualValue = repository.create(workout1);
        Assertions.assertThat(actualValue).isEqualTo(false);
    }

    @Test
    void queryWorkoutByIdSuccess() {
        WorkoutMemoryRepository repository =
                new WorkoutMemoryRepository(new ArrayList<>(List.of(workout1, workout2)));
        var spec = new WorkoutIdSpecification(1);
        List<Workout> workouts = repository.query(spec);

        Assertions.assertThat(workouts.get(0)).isEqualTo(workout1);
    }

    @Test
    void findAllWorkoutSuccess() {
        List<Workout> workoutList = new ArrayList<>();
        workoutList.add(workout1);
        workoutList.add(workout2);

        WorkoutMemoryRepository repository =
                new WorkoutMemoryRepository(new ArrayList<>(List.of(workout1, workout2)));

        Assertions.assertThat(repository.query(new WorkoutAllSpecification())).containsExactlyElementsOf(workoutList);
    }

    @Test
    void updateWorkoutSuccess() {
        WorkoutMemoryRepository repository =
                new WorkoutMemoryRepository(new ArrayList<>(List.of(workout1, workout2)));


        Assertions.assertThat(repository.update(workout1)).isEqualTo(true);
    }

    @Test
    void deleteWorkoutSuccess() {
        WorkoutMemoryRepository repository =
                new WorkoutMemoryRepository(new ArrayList<>(List.of(workout1, workout2)));

        boolean actualValue = repository.delete(workout1);
        Assertions.assertThat(actualValue).isEqualTo(true);
    }
}