package fib.br10.entity.user;

import fib.br10.core.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClientType {
    MOBILE(1),
    WEB(2);

    private final int value;

    public static ClientType fromValue(int value) {
        for (ClientType clientType : ClientType.values()) {
            if (clientType.value == value) {
                return clientType;
            }
        }
        throw new BaseException("Unknown value: " + value);
    }
}
