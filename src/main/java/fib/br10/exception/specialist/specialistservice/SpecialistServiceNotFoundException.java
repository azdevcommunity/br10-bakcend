package fib.br10.exception.specialist.specialistservice;

import fib.br10.core.exception.NotFoundException;
import fib.br10.utility.Messages;

public class SpecialistServiceNotFoundException extends NotFoundException {
    public SpecialistServiceNotFoundException(){
        super(Messages.SPECIALIST_SERVICE_NOT_FOUND);
    }
}
