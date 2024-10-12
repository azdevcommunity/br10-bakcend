package fib.br10.dto.specialist.specialistprofile.response;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class SpecialistProfileReadResponse {
    Long id;
    Long specialistUserId;
    String speciality;
    Long specialityId;
    String address;
    String city;
    String instagram;
    String tiktok;
    String facebook;
    String profilePicture;
    String username;
    String name;
    String surname;
    String phoneNumber;
}
