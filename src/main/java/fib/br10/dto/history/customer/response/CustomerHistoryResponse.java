package fib.br10.dto.history.customer.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Builder
@Data
public class CustomerHistoryResponse {
    Long reservationId;
    OffsetDateTime reservationDate;
    Integer reservationStatus;
    String specialistName;
    Long specialistId;
    String services;
    BigDecimal price;


    public CustomerHistoryResponse(Long id, OffsetDateTime reservationDate, Integer reservationStatus,
                                   String specialistUsername, Long specialistUserId,
                                   String services, BigDecimal price) {
        this.reservationId = id;
        this.reservationDate = reservationDate;
        this.reservationStatus = reservationStatus;
        this.specialistName = specialistUsername;
        this.specialistId = specialistUserId;
        this.services = services;
        this.price = price;
    }
}