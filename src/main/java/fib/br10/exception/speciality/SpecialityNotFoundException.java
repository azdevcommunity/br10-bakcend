package fib.br10.exception.speciality;

import fib.br10.core.exception.NotFoundException;

public class SpecialityNotFoundException extends NotFoundException {
    public SpecialityNotFoundException(){
        super("speciality not found");
    }
}
