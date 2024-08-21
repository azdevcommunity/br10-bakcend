package fib.br10.dto.auth.request;

import fib.br10.dto.messages.Message;
import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = Messages.REFRESH_TOKEN_REQUIRED)
    String refreshToken;
}
