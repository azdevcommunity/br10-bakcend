package fib.br10.dto.specialist.specialistprofile.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SpecialistBlockedCustomerResponse {
    Long id;
    String username;
    String phoneNumber;
    OffsetDateTime blockedDate;
}
