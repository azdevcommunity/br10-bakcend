package fib.br10.dto.specialist.specialistavailability.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecialistAvailabilityReadResponse {
    Long id;

    Long specialistUserId;

    Integer dayOfWeek;

    OffsetDateTime startTime;

    OffsetDateTime endTime;

    Boolean isWeekend;
}
