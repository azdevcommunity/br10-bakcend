package fib.br10.dto.userdevice.request;

import fib.br10.entity.user.ClientType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDeviceDto {

    Long id;

    @NotBlank
    private UUID deviceId;

    @NotNull
    private ClientType clientType;

    @NotNull
    private Long userId;

    private String operatingSystem;

    private String osVersion;

    private String appVersion;

    private String brand;

    private String model;
}
