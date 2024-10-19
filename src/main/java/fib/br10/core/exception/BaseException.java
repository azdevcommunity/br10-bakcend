package fib.br10.core.exception;

import fib.br10.utility.Messages;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@SuperBuilder
@Getter
public class BaseException extends RuntimeException{
    private final HttpStatus httpStatusCode;
    private final String message;
    private Object[] args;

    public BaseException(HttpStatus httpStatusCode, String message, Object... args) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.args = args;
    }

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
