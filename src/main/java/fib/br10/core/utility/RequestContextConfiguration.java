package fib.br10.core.utility;

import fib.br10.core.service.RequestContextProvider;
import fib.br10.exception.requestcontext.TimeZoneRequiredException;
import fib.br10.utility.JwtService;
import fib.br10.utility.Messages;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static fib.br10.core.utility.RequestContextEnum.*;

@Component
@RequiredArgsConstructor
public class RequestContextConfiguration {
    private final Localization localization;
    private final JwtService jwtService;
    private final EnvironmentUtil environmentUtil;
    private final RequestContextProvider provider;

    public void configure(HttpServletRequest request) {
        configure(
                request.getHeader(ACTIVITY_ID.getValue()),
                request.getHeader(LANG.getValue()),
                SecurityContextHolder.getContext().getAuthentication(),
                jwtService.extractJwtFromRequest(request),
                request.getHeader(TIME_ZONE.getValue()),
                request.getRemoteAddr()
        );
    }

    public void configure(String activityId, String lang, Authentication auth, String jwt, String timeZone,String ipAdress) {
        configureActivityId(activityId);
        configureLocalization(lang);
        configureAuth(auth, jwt);
        configureTimeZone(timeZone, jwt);
        configureIpAddress(ipAdress);
    }

    private void configureIpAddress(String ipAdress) {
        if(Objects.nonNull(ipAdress)){
            provider.setIpAddress(ipAdress);
        }
    }

    private void configureTimeZone(String timeZone, String jwt) {
        boolean isWhiteListed = provider.getIsPublicEndpoint();

        if (isWhiteListed && Objects.isNull(timeZone) && !environmentUtil.isDevelopment()) {
            throw new TimeZoneRequiredException();
        }

        if (!isWhiteListed) {
            timeZone = jwtService.extractClaim(jwt, ClaimTypes.TIME_ZONE);
        }

        RequestContext.set(TIME_ZONE, timeZone);
    }

    private void configureActivityId(@Nullable String activityId) {
        if (Objects.isNull(activityId)) {
            activityId = RandomUtil.getUUIDAsString();
        }

        provider.setActivityId(activityId);
        ThreadContext.put(ACTIVITY_ID.getValue(),activityId);
    }

    private void configureLocalization(String lang) {
        if (Objects.isNull(lang)) {
            lang = "az";
        }
        provider.setLang(lang);
        RequestContext.set(SUCCESS_MESSAGE, localization.getMessageOrCode(Messages.SUCCESS, new Locale(lang)));
    }

    private void configureAuth(Authentication authentication, String jwt) {
        if (Objects.isNull(authentication) || (authentication.getPrincipal().equals("anonymousUser") && provider.getIsPublicEndpoint())) {
            return;
        }

        Integer userIdInt = jwtService.extractClaim(jwt, ClaimTypes.USER_ID);

        Integer tokenId = jwtService.extractClaim(jwt, ClaimTypes.TOKEN_ID);

        Date expiration = jwtService.extractClaim(jwt, Claims::getExpiration);

        provider.setPhoneNumber(authentication.getName());
        provider.setUserId(Long.valueOf(userIdInt));
        ThreadContext.put(USER_ID.getValue(),userIdInt.toString());
        RequestContext.set(TOKEN_ID, tokenId);
        RequestContext.set(JWT_EXPIRATION, expiration);
    }
}
