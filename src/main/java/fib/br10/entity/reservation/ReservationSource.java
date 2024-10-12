package fib.br10.entity.reservation;

public enum ReservationSource {
    APP(1),
    MANUAL(2);

    private final int value;

    ReservationSource(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static ReservationSource fromValue(int value) {
        for (ReservationSource source : ReservationSource.values()) {
            if (source.value == value) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
