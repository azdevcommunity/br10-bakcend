package fib.br10.utility;

import fib.br10.configuration.SecurityEnv;
import fib.br10.core.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
@Getter
@RequiredArgsConstructor
public class SecurityUtil {

    private final SecurityEnv securityEnv;
    private final AntPathMatcher antPathMatcher;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final RequestMappingHandlerMapping controllerEndpointHandlerMapping;
    private final String swaggerEndpoint = "/**/swagger-ui/**";

    public boolean isPublicEndpoint(String endpoint) {
        boolean isWhiteListed = securityEnv.getEndpointWhiteList()
                .stream()
                .anyMatch(url -> antPathMatcher.match(url, endpoint));

        boolean isBlackListed = securityEnv.getEndpointBlackList()
                .stream()
                .anyMatch(url -> antPathMatcher.match(url, endpoint));

        return isWhiteListed && !isBlackListed;
    }

    public void validateEndpointExists(HttpServletRequest request, String endpoint) {
        try {
            if (isPublicEndpoint(endpoint) || isSwaggerEndpoint(endpoint)) {
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

    public boolean isSwaggerEndpoint(String endpoint) {
        return antPathMatcher.match(swaggerEndpoint, endpoint);
    }
}
