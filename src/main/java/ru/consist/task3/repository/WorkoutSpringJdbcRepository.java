package ru.consist.task3.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.consist.task3.model.Exercise;
import ru.consist.task3.model.Workout;
import ru.consist.task3.model.WorkoutBuilder;
import ru.consist.task3.repository.specification.Specification;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkoutSpringJdbcRepository implements ObjectRepository<Workout> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public WorkoutSpringJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Workout> query(Specification<Workout> specification) {
        List<Workout> workouts = jdbcTemplate.query("select * from workouts", (rs, rowNum) ->
                new WorkoutBuilder()
                        .setId(rs.getLong("id"))
                        .setDate(rs.getDate("date").toLocalDate())
                        .setName(rs.getString("name"))
                        .setDuration(rs.getTime("duration").toLocalTime())
                        .setKilometers(rs.getInt("kilometers"))
                        .setDescription(rs.getString("description"))
                        .setAvrHeartRate(rs.getInt("avr_heart_rate"))
                        .setCalories(rs.getInt("calories"))
                        .setHealth(rs.getString("health"))
                        .build());

        List<Long> idsWorkout = workouts.stream().map(Workout::getId).collect(Collectors.toList());
        if (idsWorkout.size() != 0) {
            var sqlParams = new MapSqlParameterSource();
            sqlParams.addValue("workoutIds", idsWorkout);

            List<Exercise> exerciseList = jdbcTemplate.query("select * from exercises where exercise_id in (:workoutIds)",
                    sqlParams, (rs, rowNum) ->
                            new Exercise(rs.getLong("exercise_id"),
                                    rs.getString("name"),
                                    rs.getInt("numRepetitions"),
                                    rs.getInt("numSets"),
                                    rs.getLong("workout_id")));

            Map<Long, List<Exercise>> exerciseByWorkoutId = new HashMap<>();
            for (Exercise exr : exerciseList) {
                exerciseByWorkoutId.putIfAbsent(exr.getWorkoutId(), new ArrayList<>());
                exerciseByWorkoutId.get(exr.getWorkoutId()).add(exr);
            }
            for (Workout workout : workouts) {
                workout.setExercises(exerciseByWorkoutId.getOrDefault(workout.getId(), new ArrayList<>()));
            }
        }

        return workouts.stream().filter(specification::specified).collect(Collectors.toList());
    }

    @Override
    public boolean create(Workout object) {
        var sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("workoutId", object.getId());
        sqlParams.addValue("workoutDate", object.getDate());
        sqlParams.addValue("workoutName", object.getName());
        sqlParams.addValue("workoutDuration", object.getDuration());
        sqlParams.addValue("workoutKilometers", object.getKilometers());
        sqlParams.addValue("workoutDescription", object.getDescription());
        sqlParams.addValue("workoutHeartRate", object.getAvrHeartRate());
        sqlParams.addValue("workoutCalories", object.getCalories());
        sqlParams.addValue("workoutHealth", object.getHealth());

        String sql = "insert into workouts (id, date, name, duration," +
                " kilometers, description, avr_heart_rate, calories, health)" +
                "values(:workoutId, :workoutDate, :workoutName, :workoutDuration," +
                " :workoutKilometers, :workoutDescription, :workoutHeartRate," +
                " :workoutCalories, :workoutHealth)";
        int createSuccess = jdbcTemplate.update(sql, sqlParams);

        List<Exercise> exercises = object.getExercises();
        if (exercises != null && exercises.size() != 0) {
            String sqlExercise = "insert into exercises  (exercise_id, name, num_repetitions, num_sets, workout_id)" +
                    " values(:exerciseId, :exerciseName, :exerciseRepet, :exerciseSets, :workoutId)";
            for (Exercise ex : exercises) {
                var sqlParamsEx = new MapSqlParameterSource();
                sqlParamsEx.addValue("exerciseId", ex.getId());
                sqlParamsEx.addValue("exerciseName", ex.getName());
                sqlParamsEx.addValue("exerciseRepet", ex.getNumRepetitions());
                sqlParamsEx.addValue("exerciseSets", ex.getNumSets());
                sqlParamsEx.addValue("workoutId", ex.getWorkoutId());
                jdbcTemplate.update(sqlExercise, sqlParamsEx);
            }
        }

        return createSuccess == 1;
    }

    @Override
    public boolean update(Workout object) {
        var sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("workoutId", object.getId());
        sqlParams.addValue("workoutDate", object.getDate());
        sqlParams.addValue("workoutName", object.getName());
        sqlParams.addValue("workoutDuration", object.getDuration());
        sqlParams.addValue("workoutKilometers", object.getKilometers());
        sqlParams.addValue("workoutDescription", object.getDescription());
        sqlParams.addValue("workoutHeartRate", object.getAvrHeartRate());
        sqlParams.addValue("workoutCalories", object.getCalories());
        sqlParams.addValue("workoutHealth", object.getHealth());

        String sql = "update workouts set " +
                "date = :workoutDate," +
                "name = :workoutName," +
                "duration = :workoutDuration," +
                "kilometers = :workoutKilometers," +
                "description = :workoutDescription," +
                "avr_heart_rate = :workoutHeartRate," +
                "calories = :workoutCalories," +
                "health = :workoutHealth " +
                "where id = :workoutId";

        int updateSuccess = jdbcTemplate.update(sql, sqlParams);
        List<Exercise> exercises = object.getExercises();
        if (exercises != null && exercises.size() != 0) {
            String sqlExercise = "update exercises set " +
                    "name = :exerciseName," +
                    "num_repetitions = :exerciseRepet," +
                    "num_sets = :exerciseSets," +
                    "workout_id = :workoutId " +
                    "where exercise_id = :exerciseId";
            for (Exercise ex : exercises) {
                var sqlParamsEx = new MapSqlParameterSource();
                sqlParamsEx.addValue("exerciseId", ex.getId());
                sqlParamsEx.addValue("exerciseName", ex.getName());
                sqlParamsEx.addValue("exerciseRepet", ex.getNumRepetitions());
                sqlParamsEx.addValue("exerciseSets", ex.getNumSets());
                sqlParamsEx.addValue("workoutId", ex.getWorkoutId());
                jdbcTemplate.update(sqlExercise, sqlParamsEx);
            }
        }
        return updateSuccess == 1;
    }

    @Override
    public boolean delete(Workout object) {
        List<Exercise> exercises = object.getExercises();
        if (exercises != null && exercises.size() != 0) {
            String sqlExercise = "delete from exercises where exercise_id = :exerciseId";
            for (Exercise ex : exercises) {
                var sqlParamsEx = new MapSqlParameterSource();
                sqlParamsEx.addValue("exerciseId", ex.getId());
                jdbcTemplate.update(sqlExercise, sqlParamsEx);
            }
        }

        var sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("workoutId", object.getId());
        String sql = "delete from workouts where id = :workoutId";
        int deleteSuccess = jdbcTemplate.update(sql, sqlParams);
        return deleteSuccess == 1;
    }
}
