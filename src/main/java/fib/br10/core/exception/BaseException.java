package fib.br10.core.exception;

import fib.br10.utility.Messages;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class BaseException extends RuntimeException{
    private final HttpStatus httpStatusCode;
    private final String message;

    public BaseException(){
        this(Messages.ERROR);
    }

    public BaseException(String message){
        this(HttpStatus.BAD_REQUEST,message);
    }

    public BaseException(Exception ex){
        this(ex.getMessage());
    }

    public BaseException(HttpStatus httpStatusCode){
        this(httpStatusCode, Messages.ERROR);
    }

    public BaseException(HttpStatus httpStatusCode, String message){
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.message=message;
    }
}
