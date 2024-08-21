package fib.br10.dto.cache;

public enum TokenType {
    ACCESS_TOKEN(1),
    REFRESH_TOKEN(2);

    private final int value;

    TokenType(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public static TokenType fromValue(int value) {
        for (TokenType type : TokenType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
