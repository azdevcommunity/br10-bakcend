package fib.br10.dto.reservation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ReservationResponse {

    public ReservationResponse(Long id) {
        this.id = id;
    }

    Long id;

    OffsetDateTime createdDate;

    OffsetDateTime reservationDate;

    Long specialistUserId;

    String specialistUsername;

    Long customerUserId;

    String customerUsername;

    String customerUserPhoneNumber;

    Long specialistServiceId;

    BigDecimal price;

    Integer reservationSource;

    Integer reservationStatus;

    Integer duration;
}
