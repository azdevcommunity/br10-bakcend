package fib.br10.exception.auth;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class OtpRequiredException extends BaseException {
    public OtpRequiredException() {
        super(Messages.OTP_REQUIRED);
    }
}
