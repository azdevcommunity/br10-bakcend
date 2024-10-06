package fib.br10.exception.notification;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class NotificationException extends BaseException {

    public NotificationException(){
        super(Messages.ERROR_OCCURRED_WHILE_SEND_NOTIFICATION);
    }
}
