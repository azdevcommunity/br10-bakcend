package fib.br10.utility;

import fib.br10.configuration.SecurityEnv;
import fib.br10.core.utility.DateUtil;
import fib.br10.exception.token.InvalidJWTClaimException;
import fib.br10.exception.token.JWTExpiredException;
import fib.br10.exception.token.JWTRequiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityEnv securityEnv;

    public String extractUsername(String token) {
        String phoneNumber = extractClaim(token, Claims::getSubject);

        if(Objects.isNull(phoneNumber)) {
            throw new InvalidJWTClaimException();
        }

        return phoneNumber;
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaim(String token, String key) {
        Claims claims = extractAllClaims(token);
        T result;
        try {
            result = (T) claims.get(key);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, securityEnv.getJwt().jwtExpiration());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, securityEnv.getJwt().jwtExpiration());
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, securityEnv.getJwt().jwtExpiration());
    }

    public void validateToken(String token, UserDetails userDetails) {
        if (!isTokenValid(token, userDetails)) {
            throw new JWTExpiredException();
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, userDetails.getUsername());
    }

    public boolean isTokenValid(String token, String phoneNumber) {
        final String username = extractUsername(token);
        return (username.equals(phoneNumber)) && !isTokenExpired(token);
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");

        if(ObjectUtils.isEmpty(jwt)) {
            return null;
        }

        if (jwt.startsWith("Bearer ")) {
            return jwt.substring(7);
        }

        return jwt;
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(DateUtil.getCurrentDate())
                .setExpiration(new Date(DateUtil.getCurrentDate().getTime() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(DateUtil.getCurrentDate());
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(securityEnv.getJwt().secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}