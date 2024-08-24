package fib.br10.exception.specialist.specialistservice;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class ServiceAlreadyUsedOnAnyReservationException extends BaseException {

    public ServiceAlreadyUsedOnAnyReservationException(){
        super(Messages.SPECIALIST_SERVICE_ALREADY_EXISTS);
    }
}
