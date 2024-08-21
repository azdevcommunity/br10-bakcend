package fib.br10.exception.specialist.specialistservice;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class SpecialistServiceAlreadyExistsException extends BaseException {
    public SpecialistServiceAlreadyExistsException(){
        super(Messages.SPECIALIST_SERVICE_ALREADY_EXISTS);
    }
}
