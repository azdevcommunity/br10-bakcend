package fib.br10.dto.auth.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetOtpRequest {

    @NotBlank(message = Messages.PHONE_NUMBER_REQUIRED)
    String phoneNumber;
}
