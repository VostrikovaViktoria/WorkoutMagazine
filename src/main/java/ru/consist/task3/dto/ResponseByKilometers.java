package ru.consist.task3.dto;

public class ResponseByKilometers {
    private long amountKilometers;

    public ResponseByKilometers(long amountKilometers) {
        this.amountKilometers = amountKilometers;
    }

    public long getAmountKilometers() {
        return amountKilometers;
    }

    public void setAmountKilometers(long amountKilometers) {
        this.amountKilometers = amountKilometers;
    }
}
