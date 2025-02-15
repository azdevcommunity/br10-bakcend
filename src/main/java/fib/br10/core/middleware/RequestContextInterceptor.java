package fib.br10.core.middleware;


import fib.br10.middleware.RequestContextConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RequestContextInterceptor implements HandlerInterceptor {
    private final RequestContextConfiguration requestContextConfiguration;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        requestContextConfiguration.configure(request);
        return true;
    }
}
