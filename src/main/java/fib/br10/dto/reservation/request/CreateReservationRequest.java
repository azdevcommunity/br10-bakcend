package fib.br10.dto.reservation.request;

import fib.br10.utility.Messages;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReservationRequest {

    @NotNull(message = Messages.RESERVATION_DATE_REQUIRED)
    OffsetDateTime reservationDate;

//    @NotNull(message = Messages.SPECIALIST_USER_ID_REQUIRED)
    Long specialistUserId;

//    @NotNull(message = Messages.CUSTOMER_USER_ID_REQUIRED)
    Long customerUserId;

    @NotNull(message = Messages.SPECIALIST_SERVICE_ID_REQUIRED)
    Long specialistServiceId;

    @NotNull(message = Messages.RESERVATION_SOURCE_REQUIRED)
    Integer reservationSource;

}
