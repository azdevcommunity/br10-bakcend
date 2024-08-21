package fib.br10.exception.user;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class UserIdRequiredException extends BaseException {

    public UserIdRequiredException() {
        super(Messages.USER_ID_REQUIRED);
    }
}
