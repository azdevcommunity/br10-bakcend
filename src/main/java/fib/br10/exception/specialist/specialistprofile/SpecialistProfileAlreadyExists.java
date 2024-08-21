package fib.br10.exception.specialist.specialistprofile;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class SpecialistProfileAlreadyExists extends BaseException {
    public SpecialistProfileAlreadyExists(){
        super(Messages.SPECIALIST_PROFILE_ALREADY_EXISTS);
    }
}
