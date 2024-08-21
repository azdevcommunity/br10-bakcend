package fib.br10.exception.token;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class InvalidJWTClaimException extends BaseException {

    public InvalidJWTClaimException(){
        super(Messages.JWT_INVALID_CLAIM);
    }
}
