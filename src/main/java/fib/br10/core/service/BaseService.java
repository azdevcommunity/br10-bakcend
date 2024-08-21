package fib.br10.core.service;


import fib.br10.core.utility.RequestContext;

import static fib.br10.core.utility.RequestContextEnum.ACTIVITY_ID;
import static fib.br10.core.utility.RequestContextEnum.USER_ID;

public abstract class BaseService {

    protected Long getUserId() {
        return RequestContext.get(USER_ID, Long.class);
    }

    protected String getActivityId() {
        return RequestContext.get(ACTIVITY_ID, String.class);
    }
}
