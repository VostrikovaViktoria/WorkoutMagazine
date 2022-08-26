package ru.consist.task3.dto;

public class ResponseByCalories {
    private long amountBurnedCalories;

    public ResponseByCalories(long amountBurnedCalories) {
        this.amountBurnedCalories = amountBurnedCalories;
    }

    public long getAmountBurnedCalories() {
        return amountBurnedCalories;
    }

    public void setAmountBurnedCalories(long amountBurnedCalories) {
        this.amountBurnedCalories = amountBurnedCalories;
    }
}
