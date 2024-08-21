package fib.br10.dto.auth.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {

    @NotBlank(message = Messages.EMAIL_REQUIRED)
    String password;

    @NotBlank(message = Messages.PASSWORD_REQUIRED)
    String confirmPassword;

    @NotBlank(message =  Messages.TOKEN_REQUIRED)
    String token;
}
