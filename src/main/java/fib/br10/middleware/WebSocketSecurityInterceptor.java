package fib.br10.middleware;


import fib.br10.core.exception.BaseException;
import fib.br10.core.utility.*;
import fib.br10.exception.token.JWTRequiredException;
import fib.br10.service.TokenService;
import fib.br10.utility.JwtService;
import fib.br10.utility.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Objects;
import java.util.stream.Stream;

import static fib.br10.core.utility.RequestContextEnum.ACTIVITY_ID;
import static fib.br10.core.utility.RequestContextEnum.LANG;
import static fib.br10.core.utility.RequestContextEnum.TIME_ZONE;


@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Log4j2
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    JwtService jwtService;
    UserDetailsService userDetailsService;
    RequestContextConfiguration requestContextConfiguration;
    TokenService tokenService;
    SecurityUtil securityUtil;
    Stream<StompCommand> WHITE_LIST = Stream.of(StompCommand.CONNECT, StompCommand.SEND, StompCommand.MESSAGE);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        intercept(message, channel);
        return message;
    }

    private void intercept(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (Objects.isNull(accessor)) {
            log.error("StompHeaderAccessor is null");
            throw new BaseException();
        }

        log.info("Accessor  command : {}", accessor.getCommand());

        if (WHITE_LIST.noneMatch(command -> command.equals(accessor.getCommand()))) {
            return;
        }

        MultiValueMap<String, String> multiValueMap = (MultiValueMap<String, String>) accessor.getHeader(StompHeaderAccessor.NATIVE_HEADERS);

        if (Objects.isNull(multiValueMap)) {
            throw new BaseException();
        }

        String jwt = multiValueMap.getFirst(RequestContextEnum.AUTHORIZATION_HEADER.getValue());

        if (Objects.isNull(jwt)) {
            throw new JWTRequiredException();
        }

        tokenService.validateTokenExistsOnBlackList(jwt);

        String phoneNumber = jwtService.extractUsername(jwt);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && !authentication.getPrincipal().equals("anonymousUser")) {
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

        jwtService.validateToken(jwt, userDetails);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        accessor.setUser(authToken);

        RequestContext.set(RequestContextEnum.IS_PUBLIC_ENDPOINT, securityUtil.isPublicEndpoint(accessor.getDestination()));

        requestContextConfiguration.configure(
                multiValueMap.getFirst(ACTIVITY_ID.getValue()),
                multiValueMap.getFirst(LANG.getValue()),
                authToken,
                jwt,
                multiValueMap.getFirst(TIME_ZONE.getValue())
        );
    }
}
