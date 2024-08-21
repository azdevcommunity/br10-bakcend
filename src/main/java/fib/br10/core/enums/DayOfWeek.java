package fib.br10.core.enums;

import fib.br10.core.entity.EntityStatus;

public enum DayOfWeek {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private final int value;

    DayOfWeek(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DayOfWeek fromValue(int value) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeek.value == value) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}