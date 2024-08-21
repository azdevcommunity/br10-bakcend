package fib.br10.exception.auth;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class OtpExpiredException extends BaseException {


public OtpExpiredException(){
    super(Messages.OTP_EXPIRED);
}
}
