package ru.consist.task3.repository.specification;

import ru.consist.task3.model.Workout;

public class WorkoutIdSpecification implements Specification<Workout> {
    private final long id;

    public WorkoutIdSpecification(long id) {
        this.id = id;
    }

    @Override
    public boolean specified(Workout workout) {
        return id == workout.getId();
    }
}
