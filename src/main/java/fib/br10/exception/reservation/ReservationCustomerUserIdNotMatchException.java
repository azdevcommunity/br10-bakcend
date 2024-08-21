package fib.br10.exception.reservation;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class ReservationCustomerUserIdNotMatchException extends BaseException {
    public ReservationCustomerUserIdNotMatchException() {
        super(Messages.RESERVATION_CUSTOMER_USER_ID_NOT_MATCH);
    }
}
