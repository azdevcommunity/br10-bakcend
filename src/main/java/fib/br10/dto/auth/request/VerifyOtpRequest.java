package fib.br10.dto.auth.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequest {

    @NotNull(message = Messages.OTP_REQUIRED)
    Integer otp;

    @NotBlank(message =  Messages.PHONE_NUMBER_REQUIRED)
    String phoneNumber;
}
