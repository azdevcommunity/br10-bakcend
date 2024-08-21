package fib.br10.core.utility;


import fib.br10.core.exception.BaseException;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
public final class RequestContext {

    private static final ThreadLocal<Map<RequestContextEnum, Object>> context = new InheritableThreadLocal<>();

    private static Map<RequestContextEnum, Object> getContextMap() {
        Map<RequestContextEnum, Object> ctx = context.get();
        if (ctx == null) {
            ctx = new HashMap<>();
            context.set(ctx);
        }
        return ctx;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(RequestContextEnum key) {
        Object value = getAsObject(key);
        try {
            return (T) value;
        } catch (ClassCastException e) {
            log.error(e);
            throw new BaseException(e);
        }
    }

    public static <T> T get(RequestContextEnum key, Class<T> type) {
        Object value = getAsObject(key);
        try {
            return type.cast(value);
        } catch (ClassCastException e) {
            log.error("Key '" + key + "' required  type: " + type.getSimpleName() + ", real type: " + value.getClass().getSimpleName(), e);
            throw new BaseException(e);
        }
    }

    public static void set(RequestContextEnum key, Object value) {
        if (Objects.isNull(value)) {
            return;
        }
        Map<RequestContextEnum, Object> ctx = getContextMap();
        ctx.put(key, value);
    }

    public static void clear() {
        context.remove();
    }

    private static Object getAsObject(RequestContextEnum key) {
        Map<RequestContextEnum, Object> ctx = getContextMap();
        return ctx.get(key);
    }
}
