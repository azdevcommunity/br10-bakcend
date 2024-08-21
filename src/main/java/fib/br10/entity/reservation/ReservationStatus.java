package fib.br10.entity.reservation;

public enum ReservationStatus {
    PENDING(1),
    SUCCESS(2),
    CANCELLED(3);

    private final int value;

    ReservationStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReservationStatus fromValue(int value) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
