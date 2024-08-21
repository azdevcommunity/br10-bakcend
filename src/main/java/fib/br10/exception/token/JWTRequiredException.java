package fib.br10.exception.token;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class JWTRequiredException extends BaseException {
    public JWTRequiredException(){
        super(Messages.JWT_REQUIRED);
    }
}
