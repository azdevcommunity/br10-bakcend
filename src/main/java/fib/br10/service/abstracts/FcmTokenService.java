package fib.br10.service.abstracts;

import fib.br10.dto.fcmtoken.request.CreateFcmTokenRequest;
import fib.br10.entity.FcmToken;

import java.util.List;

public interface FcmTokenService {

    void create(CreateFcmTokenRequest request);

    void delete(String token);

    List<String> findAllTokensByUserId(Long userId);
}
