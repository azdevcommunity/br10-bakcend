package fib.br10.core.dto;


import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

import static fib.br10.core.utility.RequestContextEnum.ACTIVITY_ID;
import static fib.br10.core.utility.RequestContextEnum.SUCCESS_MESSAGE;
import static fib.br10.core.utility.RequestContext.get;

@Data
@Builder
public class ResponseWrapper<T> implements Serializable {

    private T data;
    private Integer code;
    private String message;
    private String activityId;

    public ResponseWrapper() {
        this(null, HttpStatus.OK);
    }

    public ResponseWrapper(T data) {
        this(data, HttpStatus.OK);
    }

    public ResponseWrapper(T data, HttpStatus status) {
        this(data, status, get(SUCCESS_MESSAGE));
    }

    public ResponseWrapper(T data, HttpStatus status, String message) {
        this(data, status.value(), message, get(ACTIVITY_ID));
    }

    public ResponseWrapper(T data, Integer code, String message, String activityId) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.activityId = activityId;
    }
}
