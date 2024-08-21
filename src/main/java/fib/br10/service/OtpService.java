package fib.br10.service;

import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.DateUtil;
import fib.br10.core.utility.EnvironmentUtil;
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

    CacheService<String, CacheOtp> cacheService;
    EnvironmentUtil environmentUtil;
    RequestContextProvider provider;

    public CacheOtp add(Long userId) {

        CacheOtp cacheOtp = CacheOtp.builder()
                .userId(userId)
                .activityId(provider.getActivityId())
                .otp(RandomUtil.randomInt(1000, 9999))
                .otpExpireDate(DateUtil.getCurrentDateTime().plusMinutes(5))
                .build();

        cacheService.put(PrefixUtil.OTP + userId, cacheOtp, 5, TimeUnit.MINUTES);

        return cacheOtp;
    }

    public CacheOtp get(Long userId) {
        CacheOtp cacheOtp = cacheService.get(PrefixUtil.OTP + userId);

        if (Objects.isNull(cacheOtp)) {
            throw new OtpNotFoundException();
        }
        return cacheOtp;
    }

    public CacheOtp getAndDelete(Long userId) {
        CacheOtp cacheOtp = cacheService.getAndDelete(PrefixUtil.OTP + userId);

        if (Objects.isNull(cacheOtp)) {
            throw new OtpNotFoundException();
        }

        return cacheOtp;
    }


    public void verifyOtp(Long userId, Integer requestOtp) {
        CacheOtp cacheOtp = get(userId);

        if (Objects.isNull(cacheOtp)) {
            throw new OtpNotFoundException();
        }

//        if (environmentUtil.isDevelopment()) {
//            cacheOtp.setOtp(1234);
//        }

        if (Objects.isNull(requestOtp)) {
            throw new OtpRequiredException();
        }

        if (DateUtil.isBefore(cacheOtp.getOtpExpireDate())) {
            throw new OtpExpiredException();
        }

        if (!cacheOtp.getOtp().equals(requestOtp) || !cacheOtp.getActivityId().equals(provider.getActivityId()))  {
            throw new OtpNotalidException();
        }
    }
}
