package fib.br10.dto.specialist.specialistprofile.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSpecialistProfileRequest {
    @NotBlank(message = Messages.USER_ID_REQUIRED)
    Long specialistUserId;

    @NotNull(message = Messages.SPECIALITY_REQUIRED)
    Long specialityId;

    String address;

    String city;

    String instagram;

    String tiktok;

    String facebook;
}
