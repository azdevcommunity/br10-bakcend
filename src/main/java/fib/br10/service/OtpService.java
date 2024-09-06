package fib.br10.service;

import fib.br10.configuration.SecurityEnv;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.DateUtil;
import fib.br10.core.utility.RandomUtil;
import fib.br10.dto.cache.CacheOtp;
import fib.br10.exception.auth.OtpExpiredException;
import fib.br10.exception.auth.OtpNotFoundException;
import fib.br10.exception.auth.OtpNotalidException;
import fib.br10.exception.auth.OtpRequiredException;
import fib.br10.service.abstracts.CacheService;
import fib.br10.utility.PrefixUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OtpService {

    CacheService<String, Object> cacheService;
    RequestContextProvider provider;
    SecurityEnv securityEnv;

    public CacheOtp create(Long userId) {
        String otpCountKey = PrefixUtil.OTP_COUNT + userId;

        String otpCountStr = (String) cacheService.get(otpCountKey);
        int otpCount = otpCountStr == null ? 0 : Integer.parseInt(otpCountStr);

        if (otpCount >= securityEnv.getOtpConfig().otpTryLimit()) {
            throw new BaseException("Too many OTP requests. Please try again later.");
        }

        Integer otp = RandomUtil.randomInt(1000, 9999);
        CacheOtp cacheOtp = CacheOtp.builder()
                .activityId(provider.getActivityId())
                .otp(otp)
                .otpExpireDate(DateUtil.getCurrentDateTime().plusSeconds(securityEnv.getOtpConfig().otpExpirationTime()))
                .build();

        cacheService.put(PrefixUtil.OTP + userId, cacheOtp, securityEnv.getOtpConfig().otpExpirationTime(), TimeUnit.SECONDS);

        if (Objects.isNull(otpCountStr)) {
            cacheService.put(otpCountKey, 1, 1, TimeUnit.HOURS);
        } else {
            cacheService.increment(otpCountKey);
        }
        return cacheOtp;
    }

    public CacheOtp find(Long userId) {
        CacheOtp cacheOtp = (CacheOtp) cacheService.get(PrefixUtil.OTP + userId);

        if (Objects.isNull(cacheOtp)) {
            throw new OtpNotFoundException();
        }
        return cacheOtp;
    }

    public void verify(Long userId, Integer requestOtp) {
        CacheOtp cacheOtp = find(userId);

        if (Objects.isNull(cacheOtp)) {
            throw new OtpNotFoundException();
        }

        if (Objects.isNull(requestOtp)) {
            throw new OtpRequiredException();
        }

        if (DateUtil.isBefore(cacheOtp.getOtpExpireDate())) {
            throw new OtpExpiredException();
        }

        if (!cacheOtp.getOtp().equals(requestOtp) || !cacheOtp.getActivityId().equals(provider.getActivityId())) {
            throw new OtpNotalidException();
        }
    }
}
