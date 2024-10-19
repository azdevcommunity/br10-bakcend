package fib.br10.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationServiceResponse {

    Long detailId;
    Long serviceId;
    String serviceName;
}
