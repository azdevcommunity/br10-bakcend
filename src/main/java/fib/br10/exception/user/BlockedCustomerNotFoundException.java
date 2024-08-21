package fib.br10.exception.user;

import fib.br10.core.exception.NotFoundException;
import fib.br10.utility.Messages;

public class BlockedCustomerNotFoundException extends NotFoundException {

    public BlockedCustomerNotFoundException() {
        super(Messages.BLOCKED_CUSTOMER_NOT_FOUND);
    }
}
