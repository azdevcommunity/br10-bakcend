package fib.br10.exception.token;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class JwtAtBlackListException extends BaseException {

    public JwtAtBlackListException(){
        super(Messages.JWT_AT_BLACKLIST);
    }
}
