package fib.br10.exception.token;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class DecryptException extends BaseException {

    public DecryptException() {
        super(Messages.DECRYPT_ERROR);
    }
}
