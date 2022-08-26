package ru.consist.task3.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.consist.task3.dto.ResponseByCalories;
import ru.consist.task3.dto.ResponseByKilometers;
import ru.consist.task3.dto.ResponseByTime;

import static org.junit.jupiter.api.Assertions.*;

class ResponseMapperTest {

    @Test
    void longToResponseByKilometersSuccess() {
        var responseExpected = new ResponseByKilometers(50);
        ResponseMapper responseMapper = new ResponseMapper();

        Assertions.assertThat(responseMapper.longToResponseByKilometers(50))
                .usingRecursiveComparison()
                .isEqualTo(responseExpected);
    }

    @Test
    void stringToResponseByTimeSuccess() {
        var responseExpected = new ResponseByTime("01:30:05");
        ResponseMapper responseMapper = new ResponseMapper();

        Assertions.assertThat(responseMapper.stringToResponseByTime("01:30:05"))
                .usingRecursiveComparison()
                .isEqualTo(responseExpected);
    }

    @Test
    void longToResponseByCaloriesSuccess() {
        var responseExpected = new ResponseByCalories(150);
        ResponseMapper responseMapper = new ResponseMapper();

        Assertions.assertThat(responseMapper.longToResponseByCalories(150))
                .usingRecursiveComparison()
                .isEqualTo(responseExpected);
    }
}