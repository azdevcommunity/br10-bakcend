package fib.br10.utility;

import java.util.Objects;

public enum LangEnum {
    AZ("AZ"),
    EN("EN"),
    RU("RU");

    private final String value;

    LangEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LangEnum fromValue(String value) {
        for (LangEnum lang : LangEnum.values()) {
            if (Objects.equals(lang.value, value.toUpperCase())) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
