package fib.br10.configuration;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Log4j2
public class CorsConfig
//        implements Filter

{


//    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {


//        final HttpServletRequest request = (HttpServletRequest)req;
//        log.info("pro");

//        String dev = request.getHeader("dev") == null ? "" : request.getHeader("dev");
//
//        if(!dev.trim().isEmpty()){
//            chain.doFilter(req, res);
//            return;
//        }
//        log.info("pro2");
        final HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, enctype, Accepted-Language, TIMEZONE, ACTIVITY_ID");
        response.setHeader("Access-Control-Max-Age", "3600");
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

//    @Override
    public void destroy() {
    }

//    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
