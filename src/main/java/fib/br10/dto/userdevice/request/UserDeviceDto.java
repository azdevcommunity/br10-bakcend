package fib.br10.dto.userdevice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDeviceDto {

    Long id;

    @NotBlank
    private String deviceId;

    @NotNull
    private Integer clientType;

    private Long userId;

    private String operatingSystem;

    private String osVersion;

    private String appVersion;

    private String brand;

    private String model;
}
