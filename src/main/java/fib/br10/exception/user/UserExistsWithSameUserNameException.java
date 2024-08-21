package fib.br10.exception.user;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class UserExistsWithSameUserNameException extends BaseException{

    public UserExistsWithSameUserNameException() {
        super(Messages.USER_EXISTS_SAME_USERNAME);
    }
}
