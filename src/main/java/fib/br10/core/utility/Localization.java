package fib.br10.core.utility;

import fib.br10.utility.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Log4j2
@RequiredArgsConstructor
public class Localization {
    private final MessageSource messageSource;

    public String getMessage(String code, Locale locale, Object... args) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public String getMessage(String code, Locale locale) {
        return getMessage(code, locale, (Object) null);
    }

    public String getMessageOrCode(String code, Locale locale, Object... args) {
        String message = getMessage(code, locale, args);
        return message == null ? code : message;
    }

    public String getMessageOrCode(String code, Locale locale) {
        return getMessageOrCode(code, locale, (Object) null);
    }
}
