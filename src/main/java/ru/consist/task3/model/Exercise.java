package ru.consist.task3.model;

public class Exercise {
    private long id;

    private String name;

    private int numRepetitions;

    private int numSets;

    private long workoutId;

    public Exercise() {
    }

    public Exercise(long id, String name, int numRepetitions, int numSets, long workoutId) {
        this.id = id;
        this.name = name;
        this.numRepetitions = numRepetitions;
        this.numSets = numSets;
        this.workoutId = workoutId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumRepetitions() {
        return numRepetitions;
    }

    public void setNumRepetitions(int numRepetitions) {
        this.numRepetitions = numRepetitions;
    }

    public int getNumSets() {
        return numSets;
    }

    public void setNumSets(int numSets) {
        this.numSets = numSets;
    }

    public long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(long workoutId) {
        this.workoutId = workoutId;
    }
}
