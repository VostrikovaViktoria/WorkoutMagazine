package ru.consist.task3.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.consist.task3.model.Exercise;
import ru.consist.task3.model.Workout;
import ru.consist.task3.repository.specification.Specification;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkoutJdbcRepository implements ObjectRepository<Workout> {

    private final DataSource dataSource;

    @Autowired
    public WorkoutJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Workout> query(Specification<Workout> specification) {
        List<Workout> workouts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "select * from workouts;";
            try (Statement statement = conn.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Workout workout = new Workout();
                        workout.setId(resultSet.getLong("id"));
                        workout.setDate(resultSet.getDate("date").toLocalDate());
                        workout.setName(resultSet.getString("name"));
                        workout.setDuration(resultSet.getTime("duration").toLocalTime());
                        workout.setKilometers(resultSet.getInt("kilometers"));
                        workout.setDescription(resultSet.getString("description"));
                        workout.setAvrHeartRate(resultSet.getInt("avr_heart_rate"));
                        workout.setCalories(resultSet.getInt("calories"));
                        workout.setHealth(resultSet.getString("health"));
                        workouts.add(workout);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            List<Exercise> exerciseList = new ArrayList<>();
            try (PreparedStatement statement = conn
                    .prepareStatement("select * from exercises where exercise_id = any(?);")) {
                statement.setArray(1,
                        conn.createArrayOf("bigint", workouts.stream().map(Workout::getId).toArray()));
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Exercise exercise = new Exercise();
                        exercise.setId(resultSet.getLong("exercise_id"));
                        exercise.setName(resultSet.getString("name"));
                        exercise.setNumRepetitions(resultSet.getInt("num_repetitions"));
                        exercise.setNumSets(resultSet.getInt("num_sets"));
                        exercise.setWorkoutId(resultSet.getLong("workout_id"));
                        exerciseList.add(exercise);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Map<Long, List<Exercise>> exerciseByWorkoutId = new HashMap<>();
            for (Exercise exr : exerciseList) {
                exerciseByWorkoutId.putIfAbsent(exr.getWorkoutId(), new ArrayList<>());
                exerciseByWorkoutId.get(exr.getWorkoutId()).add(exr);
            }
            for (Workout workout : workouts) {
                workout.setExercises(exerciseByWorkoutId.getOrDefault(workout.getId(), new ArrayList<>()));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return workouts.stream().filter(specification::specified).collect(Collectors.toList());
    }

    @Override
    public boolean create(Workout object) {
        int rowsWorkoutInserted = 0;
        try (Connection conn = dataSource.getConnection()) {
            String sqlWorkout = "insert into workouts (id, date, name, duration," +
                    " kilometers, description, avr_heart_rate, calories, health)" +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement statement = conn.prepareStatement(sqlWorkout)) {
                statement.setLong(1, object.getId());
                statement.setDate(2, Date.valueOf(object.getDate()));
                statement.setString(3, object.getName());
                statement.setTime(4, Time.valueOf(object.getDuration()));
                statement.setInt(5, object.getKilometers());
                statement.setString(6, object.getDescription());
                statement.setInt(7, object.getAvrHeartRate());
                statement.setInt(8, object.getCalories());
                statement.setString(9, object.getHealth());

                rowsWorkoutInserted = statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sqlExercise = "insert into exercises  (exercise_id, name, num_repetitions, num_sets, workout_id)" +
                    " values(?, ?, ?, ?, ?);";

            List<Exercise> exercises = object.getExercises();
            if (exercises != null && exercises.size() != 0) {
                for (Exercise ex : exercises) {
                    try (PreparedStatement statement = conn.prepareStatement(sqlExercise)) {
                        statement.setLong(1, ex.getId());
                        statement.setString(2, ex.getName());
                        statement.setInt(3, ex.getNumRepetitions());
                        statement.setInt(4, ex.getNumSets());
                        statement.setLong(5, ex.getWorkoutId());

                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rowsWorkoutInserted > 0;
    }

    @Override
    public boolean update(Workout object) {
        int rowsUpdated = 0;
        try (Connection conn = dataSource.getConnection()) {
            String sql = "update workouts set " +
                    "date = ?," +
                    "name = ?," +
                    "duration = ?," +
                    "kilometers = ?," +
                    "description = ?," +
                    "avr_heart_rate = ?," +
                    "calories = ?," +
                    "health = ? " +
                    "where id = ?;";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setDate(1, Date.valueOf(object.getDate()));
                statement.setString(2, object.getName());
                statement.setTime(3, Time.valueOf(object.getDuration()));
                statement.setInt(4, object.getKilometers());
                statement.setString(5, object.getDescription());
                statement.setInt(6, object.getAvrHeartRate());
                statement.setInt(7, object.getCalories());
                statement.setString(8, object.getHealth());
                statement.setLong(9, object.getId());

                rowsUpdated = statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sqlExr = "update exercises set " +
                    "name = ?," +
                    "num_repetitions = ?," +
                    "num_sets = ?," +
                    "workout_id = ? " +
                    "where exercise_id = ?;";

            List<Exercise> exercises = object.getExercises();
            if (exercises != null && exercises.size() != 0) {
                for (Exercise ex : exercises) {
                    try (PreparedStatement statement = conn.prepareStatement(sqlExr)) {
                        statement.setString(1, ex.getName());
                        statement.setInt(2, ex.getNumRepetitions());
                        statement.setInt(3, ex.getNumSets());
                        statement.setLong(4, ex.getWorkoutId());
                        statement.setLong(5, ex.getId());

                        rowsUpdated = statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rowsUpdated > 0;
    }

    @Override
    public boolean delete(Workout object) {
        int rowDeleted = 0;
        try (Connection conn = dataSource.getConnection()) {
            List<Exercise> exercises = object.getExercises();
            if (exercises != null && exercises.size() != 0) {
                for (Exercise ex : exercises) {
                    String sqlExr = "delete from exercises where exercise_id = ?;";
                    try (PreparedStatement statement = conn.prepareStatement(sqlExr)) {
                        statement.setLong(1, ex.getId());
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            String sql = "delete from workouts where id = ?;";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, object.getId());
                rowDeleted = statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rowDeleted > 0;
    }
}
