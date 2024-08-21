package fib.br10.exception.websocket;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class WebSocketPublishException extends BaseException {

    public WebSocketPublishException() {
        super(Messages.WEBSOCKET_PUBLISH_ERROR);
    }
}
