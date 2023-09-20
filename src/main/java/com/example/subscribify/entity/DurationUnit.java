package com.example.subscribify.entity;

public enum DurationUnit {
    MONTH(1L), YEAR(12L);

    private long value;

    DurationUnit(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
