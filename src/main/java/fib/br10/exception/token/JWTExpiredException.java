package fib.br10.exception.token;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class JWTExpiredException extends BaseException {

    public JWTExpiredException() {
        super(Messages.JWT_EXPIRED);
    }
}
