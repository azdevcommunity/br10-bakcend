package fib.br10.service.abstracts;

import fib.br10.core.dto.Token;
import fib.br10.entity.user.User;
import fib.br10.dto.userdevice.request.UserDeviceDto;
import jakarta.annotation.Nullable;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Map;

public interface TokenService {

    void addTokenToBlackList(@Nullable String token);

    void addTokenToBlackList();

    boolean checkTokenExistsOnBlackList(String jwt);

    void validateTokenExistsOnBlackList(String jwt);

    Token get(User user, Map<String, Object> claims);

    Token get(User user);

    Token get(User user, UserDeviceDto deviceDto);

    Token get(User user, String key, Object value);

    void validateToken(String token, UserDetails userDetails);
}
