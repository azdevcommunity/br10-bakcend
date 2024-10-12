package fib.br10.dto.specialist.specialistprofile.request;

import fib.br10.utility.Messages;
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
public class UpdateSpecialistProfileRequest {
//
//    @NotNull(message = ValidationMessages.ID_REQUIRED)
//    Long id;

    @NotNull(message = Messages.SPECIALITY_REQUIRED)
    Long specialityId;

    String address;

    String city;

    String instagram;

    String tiktok;

    String facebook;

    String name;

    String surname;

//    MultipartFile profilePicture;
}
