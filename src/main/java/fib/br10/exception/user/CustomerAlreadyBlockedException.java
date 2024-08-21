package fib.br10.exception.user;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class CustomerAlreadyBlockedException extends BaseException {

    public CustomerAlreadyBlockedException() {
        super(Messages.CUSTOMER_ALREADY_BLOCKED);
    }
}
