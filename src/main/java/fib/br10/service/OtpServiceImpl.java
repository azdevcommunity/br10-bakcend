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
import fib.br10.service.abstracts.OtpService;
import fib.br10.utility.PrefixUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OtpServiceImpl implements OtpService {

    CacheService<String, Object> cacheService;
    RequestContextProvider provider;
    SecurityEnv securityEnv;

    public CacheOtp create(String key) {
        validateOtpRateLimit(key);
        return generateNewOtp(key);
    }

    private void validateOtpRateLimit(String key) {
        final String otpCountKey = PrefixUtil.OTP_COUNT + key;
        final String lastOtpRequestTimeKey = PrefixUtil.LAST_OTP_REQUEST_TIME + key;
        final String dailyOtpCountKey = PrefixUtil.DAILY_OTP_COUNT + key;

        // Check Last Otp Send Date
        Object lastRequestStr = cacheService.get(lastOtpRequestTimeKey);
        log.info(String.format("Last otp request key : %s, lastRequestTime: %s", lastOtpRequestTimeKey, lastRequestStr));

        if (Objects.nonNull(lastRequestStr)) {
            OffsetDateTime lastRequestTime = DateUtil.getOffsetDateTime(String.valueOf(lastRequestStr));
            if (DateUtil.getCurrentDateTime().isBefore(lastRequestTime.plusSeconds(5))) {
                throw new BaseException("You can only request a new OTP after 1 minute.");
            }
        }

        // Check Daily otp generation limit for user
        Object dailyOtpCountStr = cacheService.get(dailyOtpCountKey);
        int dailyOtpCount = dailyOtpCountStr == null ? 0 : Integer.parseInt(String.valueOf(dailyOtpCountStr));
        if (dailyOtpCount >= securityEnv.getOtpConfig().otpDailyLimit()) {
            throw new BaseException("You have reached the daily limit of 6 OTP requests.");
        }

        // Check Hour Limit for user
        Object hourOtpCountStr = cacheService.get(otpCountKey);
        int otpCount = hourOtpCountStr == null ? 0 : Integer.parseInt(String.valueOf(hourOtpCountStr));
        if (otpCount >= securityEnv.getOtpConfig().otpTryLimit()) {
            throw new BaseException("Too many OTP requests in the last hour. Please try again later.");
        }
    }

    private CacheOtp generateNewOtp(String key) {
        final String otpCountKey = PrefixUtil.OTP_COUNT + key;
        final String lastOtpRequestTimeKey = PrefixUtil.LAST_OTP_REQUEST_TIME + key;
        final String dailyOtpCountKey = PrefixUtil.DAILY_OTP_COUNT + key;

        Integer otp = RandomUtil.randomInt(1000, 9999);
        CacheOtp cacheOtp = CacheOtp.builder()
                .activityId(provider.getActivityId())
                .otp(otp)
                .otpExpireDate(DateUtil.getCurrentDateTime().plusSeconds(securityEnv.getOtpConfig().otpExpirationTime()))
                .build();

        cacheService.put(PrefixUtil.OTP + key, cacheOtp, securityEnv.getOtpConfig().otpExpirationTime(), TimeUnit.SECONDS);
        cacheService.put(lastOtpRequestTimeKey, DateUtil.getCurrentDateTime().toString(), 1, TimeUnit.MINUTES);

        Object hourOtpCountStr = cacheService.get(otpCountKey);
        if (Objects.isNull(hourOtpCountStr)) {
            cacheService.put(otpCountKey, 1, 1, TimeUnit.HOURS);
        } else {
            cacheService.increment(otpCountKey);
        }

        Object dailyOtpCountStr = cacheService.get(dailyOtpCountKey);
        if (Objects.isNull(dailyOtpCountStr)) {
            cacheService.put(dailyOtpCountKey, 1, 1, TimeUnit.DAYS);
        } else {
            cacheService.increment(dailyOtpCountKey);
        }

        return cacheOtp;
    }

    public CacheOtp find(String userId) {
        CacheOtp cacheOtp = (CacheOtp) cacheService.get(PrefixUtil.OTP + userId);

        if (Objects.isNull(cacheOtp)) {
            throw new OtpNotFoundException();
        }
        return cacheOtp;
    }

    public void verify(String key, Integer requestOtp) {
        CacheOtp cacheOtp = find(key);

        if (Objects.isNull(cacheOtp)) {
            throw new OtpNotFoundException();
        }

        if (Objects.isNull(requestOtp)) {
            throw new OtpRequiredException();
        }

        if (DateUtil.isBefore(cacheOtp.getOtpExpireDate())) {
            throw new OtpExpiredException();
        }

        if (!cacheOtp.getOtp().equals(requestOtp)) {
            throw new OtpNotalidException();
        }
    }
}
