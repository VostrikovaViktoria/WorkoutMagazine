package ru.consist.task3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.consist.task3.dto.ResponseByCalories;
import ru.consist.task3.dto.ResponseByKilometers;
import ru.consist.task3.dto.ResponseByTime;
import ru.consist.task3.dto.WorkoutDto;
import ru.consist.task3.mapper.ResponseMapper;
import ru.consist.task3.mapper.WorkoutMapper;
import ru.consist.task3.model.Workout;
import ru.consist.task3.service.WorkoutService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;
    private final ResponseMapper responseMapper;

    @Autowired
    public WorkoutController(WorkoutService workoutService, WorkoutMapper workoutMapper, ResponseMapper responseMapper) {
        this.workoutService = workoutService;
        this.workoutMapper = workoutMapper;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAllWorkouts() {
        List<Workout> workoutList = workoutService.getAllWorkouts();
        if (workoutList != null) {
            List<WorkoutDto> workoutDtoList = workoutList.stream()
                    .map(workoutMapper::workoutToWorkoutDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(workoutDtoList);
        }
        return ResponseEntity.status(404).body("Workouts not found");
    }

    @PostMapping
    public ResponseEntity<?> addNewWorkout(@RequestBody @Valid WorkoutDto workoutDto) {
        if (workoutDto == null) {
            return ResponseEntity.status(405).body("Invalid input");
        }
        Workout workout = workoutMapper.workoutDtoToWorkout(workoutDto);
        boolean workoutSaved = workoutService.saveWorkout(workout);
        if (workoutSaved) {
            return ResponseEntity.ok("Workout has been saved!");
        } else {
            return ResponseEntity.status(400).body("Invalid-id-supplied");
        }
    }

    @GetMapping("/totalCoveredKilometers")
    public ResponseEntity<?> getTotalKilometers() {
        long kilometers = workoutService.getTotalKilometers();
        ResponseByKilometers response = responseMapper.longToResponseByKilometers(kilometers);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/totalTrainingTime")
    public ResponseEntity<?> getTotalTrainingTime() {
        String time = workoutService.getTotalTrainingTime();
        ResponseByTime response = responseMapper.stringToResponseByTime(time);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/amountBurnedCalories")
    public ResponseEntity<?> getAmountBurnedCalories() {
        long calories = workoutService.getAmountBurnedCalories();
        ResponseByCalories response = responseMapper.longToResponseByCalories(calories);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<?> getWorkoutById(@PathVariable String workoutId) {
        long id;
        try {
            id = Long.parseLong(workoutId);
        } catch (NumberFormatException exception) {
            return ResponseEntity.status(400).body("Invalid-id-supplied");
        }

        Workout workout = workoutService.getWorkoutById(id);
        if (workout == null) {
            return ResponseEntity.status(404).body("Workout not found");
        }

        WorkoutDto workoutDto = workoutMapper.workoutToWorkoutDto(workout);
        return ResponseEntity.ok().body(workoutDto);
    }

    @PutMapping("/{workoutId}")
    public ResponseEntity<?> updateWorkoutById(@PathVariable String workoutId, @RequestBody @Valid WorkoutDto workoutDto) {
        long id;
        try {
            id = Long.parseLong(workoutId);
        } catch (NumberFormatException exception) {
            return ResponseEntity.status(400).body("Invalid-id-supplied");
        }

        Workout workoutUpdated = workoutService.getWorkoutById(id);
        if (workoutUpdated == null) {
            return ResponseEntity.status(404).body("Workout not found");
        }
        workoutService.updateWorkout(workoutMapper.workoutDtoToWorkout(workoutDto));
        return ResponseEntity.ok().body("Workout has been updated!");
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<?> deleteWorkoutById(@PathVariable String workoutId) {
        long id;
        try {
            id = Long.parseLong(workoutId);
        } catch (NumberFormatException exception) {
            return ResponseEntity.status(400).body("Invalid-id-supplied");
        }

        Workout workoutDeleted = workoutService.getWorkoutById(id);
        if (workoutDeleted == null) {
            return ResponseEntity.status(404).body("Workout not found");
        }

        workoutService.deleteWorkout(workoutDeleted);
        return ResponseEntity.ok().body("Workout has been removed!");
    }
}
