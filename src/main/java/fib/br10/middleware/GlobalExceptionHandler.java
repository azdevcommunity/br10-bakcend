package fib.br10.middleware;

import fib.br10.core.dto.ResponseWrapper;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.Localization;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class GlobalExceptionHandler {

    private final Localization localization;
    private final RequestContextProvider provider;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseWrapper<?>> handleBaseException(BaseException ex, WebRequest request) {
        return handleErrorResponse(ex, request, ex.getHttpStatusCode());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseWrapper<?>> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        return handleErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ResponseWrapper<?>> handleAuthenticationException(Exception ex, WebRequest request) {
        return handleErrorResponse(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<?>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        return new ResponseEntity<>(handleValidationException(ex, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<?>> handleException(Exception ex, WebRequest request) {
        return handleErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseWrapper<?>> handleErrorResponse(Exception ex, WebRequest request, HttpStatus status) {
        return new ResponseEntity<>(getResponseBody(ex, request.getLocale(), status), status);
    }

    private ResponseWrapper<?> getResponseBody(Exception exception, Locale locale, HttpStatus status) {
        String message = localization.getMessageOrCode(exception.getMessage(), locale);
        logExceptionDetails(exception,message);
        return ResponseWrapper.builder()
                .activityId(provider.getActivityId())
                .message(message)
                .code(status.value())
                .build();
    }

    private ResponseWrapper<?> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .filter(fieldError -> Objects.nonNull(localization.getMessageOrCode(fieldError.getDefaultMessage(), request.getLocale())))
                .map(fieldError -> localization.getMessageOrCode(fieldError.getDefaultMessage(), request.getLocale()))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));

        logExceptionDetails(ex, message);

        return ResponseWrapper.builder()
                .message(message)
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    private static void logExceptionDetails(Exception e, String message) {
        log.error(message, e);
    }
}
