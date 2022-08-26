package ru.consist.task3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.consist.task3.model.Workout;
import ru.consist.task3.repository.ObjectRepository;
import ru.consist.task3.repository.specification.WorkoutAllSpecification;
import ru.consist.task3.repository.specification.WorkoutIdSpecification;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final ObjectRepository<Workout> workoutRepository;

    @Autowired
    public WorkoutServiceImpl(ObjectRepository<Workout> workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    @Transactional
    public List<Workout> getAllWorkouts() {
        return workoutRepository.query(new WorkoutAllSpecification());
    }

    @Override
    @Transactional
    public Workout getWorkoutById(long id) {
        List<Workout> resultQuery = workoutRepository.query(new WorkoutIdSpecification(id));
        if (resultQuery.size() == 0) {
            return null;
        }
        return workoutRepository.query(new WorkoutIdSpecification(id)).get(0);
    }

    @Override
    @Transactional
    public boolean saveWorkout(@Valid Workout workout) {
        boolean workoutSaved = workoutRepository.create(workout);
        return workoutSaved;
    }

    @Override
    @Transactional
    public boolean updateWorkout(@Valid Workout workout) {
        boolean workoutUpdated = workoutRepository.update(workout);
        return workoutUpdated;
    }

    @Override
    @Transactional
    public boolean deleteWorkout(@Valid Workout workout) {
        boolean workoutDeleted = workoutRepository.delete(workout);
        return workoutDeleted;
    }

    @Override
    @Transactional
    public long getTotalKilometers() {
        List<Workout> workoutList = workoutRepository.query(new WorkoutAllSpecification());
        long totalKilometers = 0;
        for (Workout workout: workoutList) {
            totalKilometers += workout.getKilometers();
        }
        return totalKilometers;
    }

    @Override
    @Transactional
    public String getTotalTrainingTime() {
        List<Workout> workoutList = workoutRepository.query(new WorkoutAllSpecification());
        long totalLongTime = 0;
        for (Workout workout: workoutList) {
            LocalTime time = workout.getDuration();
            totalLongTime += time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond();
        }
        Duration duration = Duration.ofSeconds(totalLongTime);
        String hms = String.format("%02d:%02d:%02d", duration.toHours(),
                duration.toMinutesPart(), duration.toSecondsPart());
        return hms;
    }

    @Override
    @Transactional
    public long getAmountBurnedCalories() {
        List<Workout> workoutList = workoutRepository.query(new WorkoutAllSpecification());
        long amountBurnedCalories = 0;
        for (Workout workout: workoutList) {
            amountBurnedCalories += workout.getCalories();
        }
        return amountBurnedCalories;
    }
}
