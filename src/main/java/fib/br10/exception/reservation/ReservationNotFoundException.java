package fib.br10.exception.reservation;

import fib.br10.core.exception.NotFoundException;
import fib.br10.utility.Messages;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException() {
        super(Messages.RESERVATION_NOT_FOUND);
    }
}
