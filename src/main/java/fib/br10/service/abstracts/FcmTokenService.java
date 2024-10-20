package fib.br10.service.abstracts;

import fib.br10.dto.fcmtoken.request.CreateFcmTokenRequest;

public interface FcmTokenService {

     void create(CreateFcmTokenRequest request);
     void delete(String token);
}
