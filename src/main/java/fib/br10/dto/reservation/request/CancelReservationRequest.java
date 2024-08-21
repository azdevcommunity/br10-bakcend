package fib.br10.dto.reservation.request;

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
public class CancelReservationRequest {

    @NotNull(message = Messages.RESERVATION_ID_REQUIRED)
    Long reservationId;
}
