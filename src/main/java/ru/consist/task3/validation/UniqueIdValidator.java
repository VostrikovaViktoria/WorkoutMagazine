package ru.consist.task3.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.consist.task3.model.Workout;
import ru.consist.task3.repository.ObjectRepository;
import ru.consist.task3.repository.specification.WorkoutAllSpecification;
import ru.consist.task3.service.WorkoutService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueIdValidator implements ConstraintValidator<UniqueId, Long> {

    private final WorkoutService workoutService;

    @Autowired
    public UniqueIdValidator(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        long numExistsSimpleId = workoutService.getAllWorkouts()
                .stream()
                .filter(workout -> workout.getId() == id)
                .count();
        return numExistsSimpleId == 0;
    }
}
