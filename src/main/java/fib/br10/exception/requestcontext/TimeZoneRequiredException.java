package fib.br10.exception.requestcontext;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class TimeZoneRequiredException  extends BaseException {

    public TimeZoneRequiredException(){
        super(Messages.TIME_ZONE_REQUIRED);
    }
}
