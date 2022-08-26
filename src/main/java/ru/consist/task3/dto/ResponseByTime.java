package ru.consist.task3.dto;

public class ResponseByTime {
    private String amountTrainingTime;

    public ResponseByTime(String amountTrainingTime) {
        this.amountTrainingTime = amountTrainingTime;
    }

    public String getAmountTrainingTime() {
        return amountTrainingTime;
    }

    public void setAmountTrainingTime(String amountTrainingTime) {
        this.amountTrainingTime = amountTrainingTime;
    }
}
