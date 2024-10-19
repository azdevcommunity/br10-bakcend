package fib.br10.utility;

import fib.br10.configuration.SecurityEnv;
import fib.br10.core.exception.NotFoundException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Objects;
import java.util.logging.Handler;

@Component
@Getter
@RequiredArgsConstructor
public class SecurityUtil {

    private final SecurityEnv securityEnv;
    private final AntPathMatcher antPathMatcher;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final RequestMappingHandlerMapping controllerEndpointHandlerMapping;
    private final List<String> swaggerEndpoints = List.of(
            "/v3/api-docs/**", "/swagger-ui/**"
    );

    public boolean isPublicEndpoint(String endpoint) {
        boolean isWhiteListed = securityEnv.getEndpointWhiteList()
                .stream()
                .anyMatch(url -> antPathMatcher.match(url, endpoint));

        boolean isBlackListed = securityEnv.getEndpointBlackList()
                .stream()
                .anyMatch(url -> antPathMatcher.match(url, endpoint));

        return isWhiteListed && !isBlackListed;
    }

    public void validateEndpointExists(HttpServletRequest request, boolean isPublicEndpoint, String endpoint) {
        try {
            if (isPublicEndpoint || swaggerEndpoints.stream().anyMatch(url -> antPathMatcher.match(url, endpoint))) {
                return;
            }
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
            if (Objects.isNull(handlerExecutionChain)) {
                handlerExecutionChain = controllerEndpointHandlerMapping.getHandler(request);
            }
            if (Objects.isNull(handlerExecutionChain)) {
                throw new NotFoundException(request.getRequestURI());
            }
        } catch (Exception e) {
            throw new NotFoundException(request.getRequestURI());
        }
    }
}
