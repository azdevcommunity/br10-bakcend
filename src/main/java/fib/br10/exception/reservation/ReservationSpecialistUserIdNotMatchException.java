package fib.br10.exception.reservation;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class ReservationSpecialistUserIdNotMatchException extends BaseException {
    public ReservationSpecialistUserIdNotMatchException(){
        super(Messages.RESERVATION_SPECIALIST_USER_ID_NOT_MATCH);
    }
}
