package ru.consist.task3.service;


import ru.consist.task3.model.Workout;

import java.util.List;

public interface WorkoutService {
    List<Workout> getAllWorkouts();

    Workout getWorkoutById(long id);

    boolean saveWorkout(Workout workout);

    boolean updateWorkout(Workout workout);

    boolean deleteWorkout(Workout workout);

    long getTotalKilometers();

    String getTotalTrainingTime();

    long getAmountBurnedCalories();
}
