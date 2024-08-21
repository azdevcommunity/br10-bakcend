package fib.br10.dto.specialist.specialistavailability.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSpecialistAvailabilityRequest {

    @NotNull(message = "Specialist user id is required")
    DayOfWeek dayOfWeek;

    OffsetDateTime startTime;

    OffsetDateTime endTime;

    Boolean isWeekend;

}
