package fib.br10.service;

import fib.br10.dto.fcmtoken.request.CreateFcmTokenRequest;
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


    public void create(CreateFcmTokenRequest requeest){

    }

}
