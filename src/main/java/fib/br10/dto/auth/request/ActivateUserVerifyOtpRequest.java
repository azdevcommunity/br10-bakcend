package fib.br10.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import fib.br10.dto.userdevice.request.UserDeviceDto;
import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateUserVerifyOtpRequest extends VerifyOtpRequest {
    @NotNull(message = Messages.DEVICE_INFO_REQUIRED)
    @JsonProperty(value = "deviceInfo")
    private UserDeviceDto userDeviceDto;
}
