package fib.br10.dto.reservation.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationDetailResponse {
    Long id;
    Long reservationId;
    Long serviceId;
    Integer duration;
    BigDecimal price;
    String serviceName;
}
