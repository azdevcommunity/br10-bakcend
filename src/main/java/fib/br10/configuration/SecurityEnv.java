package fib.br10.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityEnv {
    private Jwt jwt;
    private List<String> endpointWhiteList;
    private List<String> endpointBlackList;
    private Encryption encryption;
    private List<String> corsAllowedOrigins;
    private OtpConfig otpConfig;
    private AuthRateLimit authRateLimit;

    public  record Jwt( String secret,int jwtExpiration,int refreshExpiration) {
    }

    public record Encryption(String secret,String algorithm,int keySize) {
    }

    public record OtpConfig(Integer otpExpirationTime, Integer otpTryLimit, Integer  otpDailyLimit)
    {}

    public record AuthRateLimit(Limitation register, Limitation login)
    {}

    public record Limitation(Integer blockTime, Integer maxAllowedAttemps){}
}