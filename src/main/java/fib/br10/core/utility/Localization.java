package fib.br10.core.utility;

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

    public String getMessage(String code, Locale locale) {
        try {
            return messageSource.getMessage(code, null, locale);
        } catch (Exception e) {
                log.error(e);
            return null;
        }
    }

    public String getMessageOrCode(String code, Locale locale) {
        String message = getMessage(code, locale);
        return message == null ? code : message;
    }
}
