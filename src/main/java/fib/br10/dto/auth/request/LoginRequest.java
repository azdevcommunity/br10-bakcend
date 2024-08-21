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
public class LoginRequest {

    @NotBlank(message = Messages.PHONE_NUMBER_OR_USERNAME_REQUIRED)
    String phoneNumberOrUsername;

    @NotBlank(message = Messages.PASSWORD_REQUIRED)
    String password;

}
