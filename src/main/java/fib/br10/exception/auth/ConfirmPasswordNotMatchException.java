package fib.br10.exception.auth;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class ConfirmPasswordNotMatchException extends BaseException {
    public ConfirmPasswordNotMatchException() {
        super(Messages.CONFIRM_PASSWORD_NOT_MATCH);
    }
}
