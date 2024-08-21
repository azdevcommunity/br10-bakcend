package fib.br10.exception.user;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class PhoneNumberRequiredException extends BaseException {
    public PhoneNumberRequiredException() {
        super(Messages.PHONE_NUMBER_REQUIRED);
    }
}
