package fib.br10.utility;

import fib.br10.configuration.SecurityEnv;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
@RequiredArgsConstructor
@Getter
public class SecurityUtil {

    private final SecurityEnv securityEnv;
    private final AntPathMatcher antPathMatcher;

    public boolean isWhiteListedEndpoint(String endpoint) {
        boolean isWhiteListed = securityEnv.getEndpointWhiteList()
                .stream()
                .anyMatch(url -> antPathMatcher.match(url, endpoint));

        boolean isBlackListed = securityEnv.getEndpointBlackList()
                .stream()
                .anyMatch(url -> antPathMatcher.match(url, endpoint));

        return isWhiteListed && !isBlackListed;
    }
}
