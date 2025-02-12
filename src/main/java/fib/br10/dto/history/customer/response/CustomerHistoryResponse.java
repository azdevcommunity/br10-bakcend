package fib.br10.dto.history.customer.response;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
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
}