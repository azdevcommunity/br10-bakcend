package fib.br10.core.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import fib.br10.core.dto.ResponseWrapper;
import fib.br10.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;


@ControllerAdvice
@RequiredArgsConstructor
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;
    private final SecurityUtil securityUtil;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResponseWrapper<?>) {
            return body;
        }

        if (securityUtil.isSwaggerEndpoint(request.getURI().getPath())) {
            return handleExceptionalEndpoints(body);
        }

        return new ResponseWrapper<>(body);
    }

    private Object handleExceptionalEndpoints(Object body) {
        try {
            return objectMapper.readTree(new String((byte[]) body, StandardCharsets.UTF_8).replace("\\\"", "\""));
        } catch (Exception e) {
            return body;
        }
    }
}