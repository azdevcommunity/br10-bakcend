package fib.br10.core.entity;

public enum EntityStatus {
    ACTIVE(1),
    DE_ACTIVE(2),
    DELETED(3);

    private final Integer value;

    EntityStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static EntityStatus fromValue(int value) {
        for (EntityStatus status : EntityStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}