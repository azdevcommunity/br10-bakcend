//package fib.br10.configuration;
//
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Log4j2
//public class CorsConfig extends HttpFilter {
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        // Handle OPTIONS requests
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setHeader("Access-Control-Allow-Origin", "http://br10.az");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
//            return;
//        }
//
//        // Continue with the next filter or the target resource
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // You can perform filter initialization here if needed
//    }
//
//    @Override
//    public void destroy() {
//        // Any cleanup can be done here
//    }
//}