package ru.consist.task3.repository;

import ru.consist.task3.model.Workout;
import ru.consist.task3.repository.specification.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutMemoryRepository implements ObjectRepository<Workout> {
    private final List<Workout> workoutList;

    public WorkoutMemoryRepository(List<Workout> workouts) {
        this.workoutList = new ArrayList<>(workouts);
    }

    @Override
    public List<Workout> query(Specification<Workout> specification) {
        return workoutList.stream().filter(specification::specified).collect(Collectors.toList());
    }

    @Override
    public boolean create(Workout workoutCreate) {
        boolean hasNotWorkoutWithSameId = workoutList.stream()
                .noneMatch(workout -> workout.getId() == workoutCreate.getId());
        if (hasNotWorkoutWithSameId) {
            return workoutList.add(workoutCreate);
        }
        return false;
    }

    @Override
    public boolean update(Workout workoutUpdated) {
        for (Workout workout: workoutList) {
            if (workout.getId() == workoutUpdated.getId()) {
                int index = workoutList.indexOf(workout);
                workoutList.remove(workout);
                workoutList.add(index, workoutUpdated);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Workout workout) {
        return workoutList.remove(workout);
    }
}
