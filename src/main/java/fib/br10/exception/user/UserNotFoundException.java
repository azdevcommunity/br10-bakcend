package fib.br10.exception.user;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(){
        super(Messages.USER_NOT_FOUND);
    }
}
