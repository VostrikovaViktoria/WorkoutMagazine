package ru.consist.task3.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkoutBuilder {
    private long id;
    private LocalDate date;
    private String name;
    private LocalTime duration;
    private int kilometers;
    private String description;
    private int avrHeartRate;
    private int calories;
    private String health;

    public WorkoutBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public WorkoutBuilder setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public WorkoutBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public WorkoutBuilder setDuration(LocalTime duration) {
        this.duration = duration;
        return this;
    }

    public WorkoutBuilder setKilometers(int kilometers) {
        this.kilometers = kilometers;
        return this;
    }

    public WorkoutBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public WorkoutBuilder setAvrHeartRate(int avrHeartRate) {
        this.avrHeartRate = avrHeartRate;
        return this;
    }

    public WorkoutBuilder setCalories(int calories) {
        this.calories = calories;
        return this;
    }

    public WorkoutBuilder setHealth(String health) {
        this.health = health;
        return this;
    }

    public Workout build() {
        return new Workout(id, date, name, duration, kilometers, description, avrHeartRate, calories, health);
    }
}