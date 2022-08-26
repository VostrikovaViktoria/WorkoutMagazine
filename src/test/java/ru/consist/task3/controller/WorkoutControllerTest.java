package ru.consist.task3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import ru.consist.task3.dto.ResponseByCalories;
import ru.consist.task3.dto.ResponseByKilometers;
import ru.consist.task3.dto.ResponseByTime;
import ru.consist.task3.dto.WorkoutDto;
import ru.consist.task3.mapper.ResponseMapper;
import ru.consist.task3.mapper.WorkoutMapper;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;
import ru.consist.task3.service.WorkoutService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private WorkoutMapper workoutMapper;

    @MockBean
    private ResponseMapper responseMapper;

    @Test
    void getAllWorkoutsSuccess() throws Exception {
        List<Workout> workoutList = new ArrayList<>();
        Workout workout1 = new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();
        Workout workout2 = new WorkoutBuilder()
                .setId(2)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Силовая")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(0)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Прекрасное состояние")
                .build();
        workoutList.add(workout1);
        workoutList.add(workout2);

        WorkoutDto workoutDto1 = new WorkoutDto(
                1,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );
        WorkoutDto workoutDto2 = new WorkoutDto(
                2,
                LocalDate.of(2022, 4, 5),
                "Силовая",
                LocalTime.of(0, 30, 13),
                0,
                "description",
                170,
                200,
                "Прекрасное состояние"
        );

        Mockito.when(workoutService.getAllWorkouts()).thenReturn(workoutList);
        Mockito.when(workoutMapper.workoutToWorkoutDto(workout1)).thenReturn(workoutDto1);
        Mockito.when(workoutMapper.workoutToWorkoutDto(workout2)).thenReturn(workoutDto2);

        mockMvc.perform(get("/workouts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "  \"id\":1," +
                        "  \"date\":\"2022-04-05\"," +
                        "  \"name\":\"Кросс\"," +
                        "  \"duration\":\"00:30:13\"," +
                        "  \"kilometers\":5," +
                        "  \"description\":\"description\"," +
                        "  \"avrHeartRate\":170," +
                        "  \"calories\":200," +
                        "  \"health\":\"Хорошее состояние\"" +
                        "}, {" +
                        "  \"id\":2," +
                        "  \"date\":\"2022-04-05\"," +
                        "  \"name\":\"Силовая\"," +
                        "  \"duration\":\"00:30:13\"," +
                        "  \"kilometers\":0," +
                        "  \"description\":\"description\"," +
                        "  \"avrHeartRate\":170," +
                        "  \"calories\":200," +
                        "  \"health\":\"Прекрасное состояние\"" +
                        "}]"
                ));
    }

    @Test
    void listWorkoutsNotFound() throws Exception {
        Mockito.when(workoutService.getAllWorkouts()).thenReturn(null);

        mockMvc.perform(get("/workouts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string("Workouts not found"));
    }

    @Test
    void getWorkoutByIdSuccess() throws Exception {
        Workout workout = new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        WorkoutDto workoutDto = new WorkoutDto(
                1,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );

        Mockito.when(workoutService.getWorkoutById(1L)).thenReturn(workout);
        Mockito.when(workoutMapper.workoutToWorkoutDto(workout)).thenReturn(workoutDto);

        mockMvc.perform(get("/workouts/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"id\":1," +
                        "  \"date\":\"2022-04-05\"," +
                        "  \"name\":\"Кросс\"," +
                        "  \"duration\":\"00:30:13\"," +
                        "  \"kilometers\":5," +
                        "  \"description\":\"description\"," +
                        "  \"avrHeartRate\":170," +
                        "  \"calories\":200," +
                        "  \"health\":\"Хорошее состояние\"" +
                        "}"
                ));
    }

    @Test
    void workoutByIdForGetNotFound() throws Exception {
        Mockito.when(workoutService.getWorkoutById(5)).thenReturn(null);

        mockMvc.perform(get("/workouts/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string("Workout not found"));
    }

    @Test
    void idWorkoutForGetNotSupplied() throws Exception {
        mockMvc.perform(get("/workouts/lp")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(content().string("Invalid-id-supplied"));
    }

    @Test
    void saveWorkoutSuccess() throws Exception {
        var workout = new WorkoutBuilder()
                .setId(3)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        WorkoutDto workoutDto = new WorkoutDto(
                3,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );

        Mockito.when(workoutMapper.workoutDtoToWorkout(Mockito.any(WorkoutDto.class))).thenReturn(workout);
        Mockito.when(workoutService.saveWorkout(Mockito.any(Workout.class))).thenReturn(true);

        String content = objectWriter.writeValueAsString(workoutDto);
        mockMvc.perform(post("/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().string("Workout has been saved!"));
    }

    @Test
    public void updateWorkoutSuccess() throws Exception {
        var workout = new WorkoutBuilder()
                .setId(3)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние").build();

        WorkoutDto workoutDto = new WorkoutDto(
                3,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );

        Mockito.when(workoutService.getWorkoutById(workout.getId())).thenReturn(workout);
        Mockito.when(workoutMapper.workoutDtoToWorkout(workoutDto)).thenReturn(workout);
        Mockito.when(workoutService.updateWorkout(workout)).thenReturn(true);

        String content = objectWriter.writeValueAsString(workoutDto);
        mockMvc.perform(put("/workouts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().string("Workout has been updated!"));
    }

    @Test
    public void workoutForUpdateNotFound() throws Exception {
        var workout = new WorkoutBuilder()
                .setId(2)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        WorkoutDto workoutDto = new WorkoutDto(
                2,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );

        Mockito.when(workoutService.getWorkoutById(workout.getId())).thenReturn(null);

        String content = objectWriter.writeValueAsString(workoutDto);
        mockMvc.perform(put("/workouts/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is(404))
                .andExpect(content().string("Workout not found"));
    }

    @Test
    public void updateIdWorkoutNotSupplied() throws Exception {
        var workout = new WorkoutBuilder()
                .setId(2)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5)
                .setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        WorkoutDto workoutDto = new WorkoutDto(
                2,
                LocalDate.of(2022, 4, 5),
                "Кросс",
                LocalTime.of(0, 30, 13),
                5,
                "description",
                170,
                200,
                "Хорошее состояние"
        );

        String content = objectWriter.writeValueAsString(workoutDto);
        mockMvc.perform(put("/workouts/lpl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(400))
                .andExpect(content().string("Invalid-id-supplied"));
    }

    @Test
    public void deleteWorkoutById() throws Exception {
        var workout = new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5).setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        Mockito.when(workoutService.getWorkoutById(workout.getId())).thenReturn(workout);
        Mockito.when(workoutService.deleteWorkout(workout)).thenReturn(true);
        mockMvc.perform(delete("/workouts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Workout has been removed!"));
    }

    @Test
    public void deleteWorkoutIdNotSupplied() throws Exception {
         mockMvc.perform(delete("/workouts/ljb")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(content().string("Invalid-id-supplied"));
    }

    @Test
    public void workoutForDeleteNotFound() throws Exception {
        var workout = new WorkoutBuilder()
                .setId(1)
                .setDate(LocalDate.of(2022, 4, 5))
                .setName("Кросс")
                .setDuration(LocalTime.of(0, 30, 13))
                .setKilometers(5).setDescription("description")
                .setAvrHeartRate(170)
                .setCalories(200)
                .setHealth("Хорошее состояние")
                .build();

        Mockito.when(workoutService.getWorkoutById(workout.getId())).thenReturn(null);

        mockMvc.perform(delete("/workouts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string("Workout not found"));
    }

    @Test
    void getKilometersSuccess() throws Exception {
        Mockito.when(workoutService.getTotalKilometers()).thenReturn(Long.valueOf(50));
        Mockito.when(responseMapper.longToResponseByKilometers(50))
                .thenReturn(new ResponseByKilometers(50));

        mockMvc.perform(get("/workouts/totalCoveredKilometers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{" +
                                "\"amountKilometers\":50" +
                                "}"
                ));
    }

    @Test
    void getTotalTrainingTime() throws Exception {
        Mockito.when(workoutService.getTotalTrainingTime()).thenReturn("01:30:05");
        Mockito.when(responseMapper.stringToResponseByTime("01:30:05"))
                .thenReturn(new ResponseByTime("01:30:05"));

        mockMvc.perform(get("/workouts/totalTrainingTime")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{" +
                                "\"amountTrainingTime\":\"01:30:05\"" +
                                "}"
                ));
    }

    @Test
    void getAmountBurnedCalories() throws Exception {
        Mockito.when(workoutService.getAmountBurnedCalories()).thenReturn(Long.valueOf(300));
        Mockito.when(responseMapper.longToResponseByCalories(300))
                .thenReturn(new ResponseByCalories(300));

        mockMvc.perform(get("/workouts/amountBurnedCalories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{" +
                                "\"amountBurnedCalories\":300" +
                                "}"
                ));
    }
}
