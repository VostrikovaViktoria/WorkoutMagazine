package ru.consist.task3.mapper;

import org.springframework.stereotype.Component;
import ru.consist.task3.dto.ResponseByCalories;
import ru.consist.task3.dto.ResponseByKilometers;
import ru.consist.task3.dto.ResponseByTime;

@Component
public class ResponseMapper {
    public ResponseByKilometers longToResponseByKilometers(long kilometers) {
        return new ResponseByKilometers(kilometers);
    }

    public ResponseByTime stringToResponseByTime(String time) {
        return new ResponseByTime(time);
    }

    public ResponseByCalories longToResponseByCalories(long calories) {
        return new ResponseByCalories(calories);
    }
}
