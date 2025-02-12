package fib.br10.dto.history.customer.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerHistoryDetailsDTO {
    private Long reservationId;
    private OffsetDateTime reservationDate;
    private Integer reservationStatus;
    private String specialistName;
    private Long specialistId;
    private BigDecimal price;
    private List<ServiceResponseDTO> services;
}

