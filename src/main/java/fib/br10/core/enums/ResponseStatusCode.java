package fib.br10.core.enums;

public enum ResponseStatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404);

    private final int value;

    ResponseStatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ResponseStatusCode fromValue(int value) {
        for (ResponseStatusCode statusCode : ResponseStatusCode.values()) {
            if (statusCode.value == value) {
                return statusCode;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
