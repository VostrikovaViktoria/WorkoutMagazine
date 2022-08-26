package ru.consist.task3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import ru.consist.task3.validation.UniqueId;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class WorkoutDto {
    @UniqueId(message = "Тренировка с таким идентификатором уже существует.")
    private long id;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate date;

    @NotBlank(message = "Название тренировки не должно быть пустым.")
    private String name;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime duration;

    @PositiveOrZero(message = "Количество километров не должно быть отрицательным.")
    private int kilometers;

    private String description;

    @PositiveOrZero(message = "Среднее значение пульса не должно быть отрицательным.")
    private int avrHeartRate;

    @PositiveOrZero(message = "Количество калорий не должно быть отрицательным.")
    private int calories;

    private String health;

    public WorkoutDto() {
    }

    public WorkoutDto(long id, LocalDate date, String name, LocalTime duration, int kilometers, String description,
                      int avrHeartRate, int calories, String health) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.duration = duration;
        this.kilometers = kilometers;
        this.description = description;
        this.avrHeartRate = avrHeartRate;
        this.calories = calories;
        this.health = health;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvrHeartRate() {
        return avrHeartRate;
    }

    public void setAvrHeartRate(int avrHeartRate) {
        this.avrHeartRate = avrHeartRate;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }
}
