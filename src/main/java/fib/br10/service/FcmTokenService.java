package fib.br10.service;

import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.fcmtoken.request.CreateFcmTokenRequest;
import fib.br10.entity.FcmToken;
import fib.br10.mapper.FcmTokenMapper;
import fib.br10.repository.FcmTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FcmTokenService {
    FcmTokenRepository fcmTokenRepository;
    FcmTokenMapper fcmTokenMapper;
    RequestContextProvider provider;

    public void create(CreateFcmTokenRequest request) {
        FcmToken fcmToken = fcmTokenMapper.createFcmTokenToFcmToken(request, provider.getUserId());
        fcmTokenRepository.save(fcmToken);
    }

    public void delete(String token) {
        fcmTokenRepository.findByToken(token).ifPresent(fcmTokenRepository::delete);
    }
}
