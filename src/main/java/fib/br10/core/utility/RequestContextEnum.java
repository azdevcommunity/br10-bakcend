package fib.br10.core.utility;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

import java.util.Objects;

public enum RequestContextEnum {

    ACTIVITY_ID("ACTIVITY_ID"),
    LANG("Accept-Language"),
    SUCCESS_MESSAGE(Messages.SUCCESS),
    PHONE_NUMBER("PHONE_NUMBER"),
    USER_ID("USER_ID"),
    TIME_ZONE("TIME_ZONE"),
    REQUEST_PATH("REQUEST_PATH"),
    IS_PUBLIC_ENDPOINT("IS_PUBLIC_ENDPOINT"),
    TOKEN_ID("TOKEN_ID"),
    JWT_EXPIRATION("JWT_EXPIRATION"),
    AUTHORIZATION_HEADER("AUTHORIZATION"),
    CLIENT_TYPE("CLIENT_TYPE"),
    CLIENT_IP("CLIENT_IP");

    private final String value;

    RequestContextEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequestContextEnum fromValue(String value) {
        for (RequestContextEnum status : RequestContextEnum.values()) {
            if (Objects.equals(status.value, value)) {
                return status;
            }
        }
        throw new BaseException("Unknown value: " + value);
    }
}
