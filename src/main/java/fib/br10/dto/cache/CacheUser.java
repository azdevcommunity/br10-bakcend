package fib.br10.dto.cache;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CacheUser implements Serializable {
    String username;

    String password;

    String confirmPassword;

    String phoneNumber;

    Long specialityId;

    Integer registerType;

    String activityId;
}
