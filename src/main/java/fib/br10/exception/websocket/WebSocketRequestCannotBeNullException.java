package fib.br10.exception.websocket;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class WebSocketRequestCannotBeNullException extends BaseException {
    public WebSocketRequestCannotBeNullException() {
        super(Messages.WEBSOCKET_REQUEST_CANNOT_BE_NULL);
    }
}
