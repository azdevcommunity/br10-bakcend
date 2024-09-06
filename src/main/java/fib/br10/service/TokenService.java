package fib.br10.service;

import fib.br10.core.dto.Token;
import fib.br10.core.dto.UserDetailModel;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.ClaimTypes;
import fib.br10.core.utility.DateUtil;
import fib.br10.dto.userdevice.request.UserDeviceDto;
import fib.br10.exception.token.InvalidJWTClaimException;
import fib.br10.exception.token.JWTExpiredException;
import fib.br10.utility.JwtService;
import fib.br10.core.utility.RandomUtil;
import fib.br10.dto.cache.CacheToken;
import fib.br10.dto.cache.TokenType;
import fib.br10.entity.user.User;
import fib.br10.mapper.UserMapper;
import fib.br10.service.abstracts.CacheService;
import fib.br10.utility.PrefixUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;


import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TokenService {

    UserMapper userMapper;
    JwtService jwtService;
    CacheService<String, CacheToken> cacheService;
    RequestContextProvider provider;

    public void addTokenToBlackList(@Nullable String token) {
        if (Objects.isNull(token)) {
            return;
        }

        Integer userIdInt = jwtService.extractClaim(token, ClaimTypes.USER_ID);

        Integer tokenId = jwtService.extractClaim(token, ClaimTypes.TOKEN_ID);

        long expiration = DateUtil.getDifferenceAsLong(jwtService.extractClaim(token, Claims::getExpiration));

        addTokenToBlackList(Long.valueOf(userIdInt), tokenId, expiration);
    }

    public void addTokenToBlackList() {
        Integer tokenId = provider.getTokenId();
        Long userId = provider.getUserId();

        long expiration = DateUtil.getDifferenceAsLong(provider.getJwtExpiration());

        addTokenToBlackList(userId, tokenId, expiration);
    }

    private void addTokenToBlackList(Long userId, Integer tokenId, long expiration) {
        CacheToken cacheToken = CacheToken.builder()
                .tokenId(tokenId)
                .tokenType(TokenType.ACCESS_TOKEN.get())
                .build();

        cacheService.put(PrefixUtil.TOKEN + userId + "_" + tokenId, cacheToken, expiration, TimeUnit.SECONDS);
    }


    public boolean checkTokenExistsOnBlackList(String jwt) {
        final Integer userId = jwtService.extractClaim(jwt, ClaimTypes.USER_ID);
        final Integer tokenId = jwtService.extractClaim(jwt, ClaimTypes.TOKEN_ID);

        final String key = PrefixUtil.TOKEN + userId + "_" + tokenId;

        CacheToken cacheToken = cacheService.get(key);

        return Objects.nonNull(cacheToken);
    }

    public void validateTokenExistsOnBlackList(String jwt) {
        if (checkTokenExistsOnBlackList(jwt)) {
            throw new JWTExpiredException();
        }
    }


    public Token get(User user, Map<String, Object> claims) {
        UserDetailModel userDetailModel = userMapper.userToUserDetails(new UserDetailModel(), user);

        claims.put(ClaimTypes.TIME_ZONE, provider.getTimeZone());
        claims.put(ClaimTypes.USER_ID, user.getId());
        claims.put(ClaimTypes.TOKEN_ID, RandomUtil.randomInt(100000, 999999));

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put(ClaimTypes.TOKEN_ID, RandomUtil.randomInt(100000, 999999));
        refreshClaims.put(ClaimTypes.USER_ID, user.getId());

        return Token.builder()
                .accessToken(jwtService.generateToken(claims, userDetailModel))
                .refreshToken(jwtService.generateRefreshToken(refreshClaims, userDetailModel))
                .build();
    }

    public Token get(User user) {
        return get(user, new HashMap<>());
    }

    public Token get(User user, UserDeviceDto deviceDto) {
        return get(user, Map.ofEntries(
                        Map.entry(ClaimTypes.DEVICE_ID, deviceDto.getId()),
                        Map.entry(ClaimTypes.CLIENT_TYPE, deviceDto.getClientType().getValue())
                )
        );
    }

    public Token get(User user, String key, Object value) {
        return get(user, Map.ofEntries(Map.entry(key, value)));
    }

    public void validateToken(String token, UserDetails userDetails) {
        if (jwtService.isTokenExpired(token)) {
            throw new JWTExpiredException();
        }

        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new InvalidJWTClaimException();
        }
    }
}
