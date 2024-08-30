package fib.br10.utility;

import fib.br10.core.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum LangEnum {
    AZ("AZ",1),
    EN("EN",2),
    RU("RU",3);

    private final String value;
    private final Integer code;

    public static LangEnum fromValue(String value) {
        for (LangEnum lang : LangEnum.values()) {
            if (Objects.equals(lang.value, value.toUpperCase())) {
                return lang;
            }
        }
        throw new BaseException("Unknown value: " + value);
    }

    public static LangEnum fromCode(Integer code) {
        for (LangEnum lang : LangEnum.values()) {
            if (Objects.equals(lang.code, code)) {
                return lang;
            }
        }
        throw new BaseException("Unknown value: " + code);
    }
}
