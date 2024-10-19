package fib.br10.enumeration;

import fib.br10.core.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegisterType {
    CLIENT(1),
    SPECIALIST(2);

    private final Integer value;

    public static RegisterType fromValue(int value) {
        for (RegisterType clientType : RegisterType.values()) {
            if (clientType.value == value) {
                return clientType;
            }
        }
        throw new BaseException("Unknown value for RegisterType value is: " + value);
    }
}
