package fib.br10.exception.specialist.specialistprofile;

import fib.br10.core.exception.NotFoundException;
import fib.br10.utility.Messages;

public class SpecialistProfileNotFoundException extends NotFoundException {
    public SpecialistProfileNotFoundException(){
        super(Messages.SPECIALIST_PROFILE_NOT_FOUND);
    }
}
