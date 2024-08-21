package fib.br10.entity.user;

public enum RoleEnum {
    CUSTOMER(1),
    SPECIALIST(2),
    ADMIN(3);

    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoleEnum fromValue(int value) {
        for (RoleEnum status : RoleEnum.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
