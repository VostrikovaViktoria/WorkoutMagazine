package ru.consist.task3.repository.specification;

import ru.consist.task3.model.Workout;

public class WorkoutAllSpecification implements Specification<Workout> {
    @Override
    public boolean specified(Workout object) {
        return true;
    }
}
