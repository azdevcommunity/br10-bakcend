package fib.br10.dto.auth.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = Messages.USERNAME_REQUIRED)
    String username;

    @NotBlank(message = Messages.EMAIL_REQUIRED)
    String password;

    @NotBlank(message = Messages.PASSWORD_REQUIRED)
    String confirmPassword;

    @NotBlank(message = Messages.PHONE_NUMBER_REQUIRED)
    String phoneNumber;

    Long specialityId;

    @NotNull(message = Messages.REGISTER_TYPE_REQUIRED)
    Integer registerType;

    String activityId;
}