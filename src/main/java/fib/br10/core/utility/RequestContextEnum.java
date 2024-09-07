package fib.br10.core.utility;

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
    AUTHORIZATION_HEADER("AUTHORIZATION_HEADER");

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
        throw new IllegalArgumentException("Unknown value: " + value);
    }

//    public final static  String ACTIVITY_ID = "ACTIVITY_ID";
//    public final static  String LANG = "Accept-Language";
//    public final static  String SUCCESS_MESSAGE = Messages.SUCCESS;
//    public final static  String PHONE_NUMBER = "PHONE_NUMBER";
//    public final static  String CURRENT_USER_ID = "CURRENT_USER_ID";
//    public final static  String TIME_ZONE = "TIME_ZONE";
//    public final static  String REQUEST_PATH = "REQUEST_PATH";
//    public final static  String IS_WHITE_LISTED_ENDPOINT = "IS_WHITE_LISTED_ENDPOINT";
//    public final static  String TOKEN_ID = "TOKEN_ID";
//    public final static  String JWT_EXPIRATION= "JWT_EXPIRATION";
//    public final static  String AUTHORIZATION_HEADER= "Authorization";

}
