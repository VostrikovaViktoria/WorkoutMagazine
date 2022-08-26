package ru.consist.task3.mapper;

import org.springframework.stereotype.Component;
import ru.consist.task3.dto.WorkoutDto;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;

@Component
public class WorkoutMapper {
    public WorkoutDto workoutToWorkoutDto(Workout workout) {
        return new WorkoutDto(
                workout.getId(),
                workout.getDate(),
                workout.getName(),
                workout.getDuration(),
                workout.getKilometers(),
                workout.getDescription(),
                workout.getAvrHeartRate(),
                workout.getCalories(),
                workout.getHealth()
                );
    }

    public Workout workoutDtoToWorkout(WorkoutDto workoutDto) {
        return new WorkoutBuilder()
                .setId(workoutDto.getId())
                .setDate(workoutDto.getDate())
                .setName(workoutDto.getName())
                .setDuration(workoutDto.getDuration())
                .setKilometers(workoutDto.getKilometers())
                .setDescription(workoutDto.getDescription())
                .setAvrHeartRate(workoutDto.getAvrHeartRate())
                .setCalories(workoutDto.getCalories())
                .setHealth(workoutDto.getHealth())
                .build();
    }
}
