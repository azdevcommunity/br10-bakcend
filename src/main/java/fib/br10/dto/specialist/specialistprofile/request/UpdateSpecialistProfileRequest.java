package fib.br10.dto.specialist.specialistprofile.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSpecialistProfileRequest {
//
//    @NotNull(message = ValidationMessages.ID_REQUIRED)
//    Long id;

    @NotBlank(message = Messages.SPECIALITY_REQUIRED)
    String speciality;

    String address;

    String city;

    String instagram;

    String tiktok;

    String facebook;

    MultipartFile profilePicture;
}
