package fib.br10.service.abstracts;

import fib.br10.dto.cache.CacheOtp;

public interface OtpService {

    CacheOtp create(String key);

    CacheOtp find(String userId);

    void verify(String key, Integer requestOtp);
}
