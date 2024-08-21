package fib.br10.exception.token;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class TokenNotValidException extends BaseException {
    public TokenNotValidException() {
        super(Messages.TOKEN_NOT_VALID);
    }

}
