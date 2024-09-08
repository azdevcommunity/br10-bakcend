package fib.br10.entity.user;

import fib.br10.core.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    CUSTOMER(1),
    SPECIALIST(2),
    ADMIN(3);

    private final int value;

    public static RoleEnum fromValue(int value) {
        for (RoleEnum status : RoleEnum.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new BaseException("Unknown value: " + value);
    }
}
