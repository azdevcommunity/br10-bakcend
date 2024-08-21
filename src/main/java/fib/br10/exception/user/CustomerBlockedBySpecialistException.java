package fib.br10.exception.user;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class CustomerBlockedBySpecialistException extends BaseException {

    public CustomerBlockedBySpecialistException() {
        super(Messages.CUSTOMER_BLOCKED_BY_SPECIALIST);
    }
}
