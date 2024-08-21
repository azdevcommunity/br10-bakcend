package fib.br10.exception.token;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class EncryptException extends BaseException {

    public EncryptException() {
        super(Messages.ENCRYPT_ERROR);
    }
}
