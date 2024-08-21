package fib.br10.exception.auth;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class OtpNotFoundException extends BaseException {

    public OtpNotFoundException(){
        super(Messages.OTP_NOT_FOUND);
    }
}

