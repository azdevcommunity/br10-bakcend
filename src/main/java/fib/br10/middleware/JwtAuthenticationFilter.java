package fib.br10.middleware;


import com.fasterxml.jackson.core.JsonEncoding;
import fib.br10.core.dto.ResponseWrapper;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.JsonSerializer;
import fib.br10.core.utility.Localization;
import fib.br10.core.utility.RequestContext;
import fib.br10.core.utility.RequestContextEnum;
import fib.br10.exception.token.JWTRequiredException;
import fib.br10.service.TokenService;
import fib.br10.utility.JwtService;
import fib.br10.utility.Messages;
import fib.br10.utility.SecurityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final JsonSerializer jsonSerializer;
    private final Localization localization;
    private final TokenService tokenService;
    private final RequestContextProvider provider;
    private final SecurityUtil securityUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) {
        try {
            String path = request.getServletPath();
            boolean isPublicEndpoint = securityUtil.isPublicEndpoint(path);
            securityUtil.validateEndpointExists(request, isPublicEndpoint);
            provider.setRequestPath(path);
            provider.setIsPublicEnpoint(isPublicEndpoint);

            if (isPublicEndpoint) {
                filterChain.doFilter(request, response);
                return;
            }
            final String jwt = jwtService.extractJwtFromRequest(request);

            if (Objects.isNull(jwt)) {
                throw new JWTRequiredException();
            }
            tokenService.validateTokenExistsOnBlackList(jwt);
            final String phoneNumber = jwtService.extractUsername(jwt);
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (Objects.isNull(authentication) || authentication.getPrincipal().equals("anonymousUser")) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(phoneNumber);
                jwtService.validateToken(jwt, userDetails);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                RequestContext.set(RequestContextEnum.AUTHORIZATION_HEADER, jwt);
                provider.setAuthorizationHeader(jwt);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (BaseException | ExpiredJwtException | UsernameNotFoundException e) {
            log.error(e);
            modifyResponseBody(request.getLocale(), response, e.getMessage());
        } catch (Exception e) {
            log.error(e);
            modifyResponseBody(request.getLocale(), response, Messages.ERROR);
        }
    }

    private void modifyResponseBody(Locale locale, HttpServletResponse response, String message) {
        ResponseWrapper<Object> body = ResponseWrapper.builder()
                .code(HttpServletResponse.SC_UNAUTHORIZED)
                .message(localization.getMessageOrCode(message, locale))
                .data(null)
                .build();

        String jsonResponse = jsonSerializer.serialize(body);

        if (Objects.isNull(jsonResponse)) {
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(JsonEncoding.UTF8.getJavaName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try {
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error(e);
        }
    }
}