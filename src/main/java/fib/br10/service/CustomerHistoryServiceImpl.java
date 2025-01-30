package fib.br10.service;

import fib.br10.dto.history.customer.response.CustomerHistoryResponse;
import fib.br10.dto.reservation.response.ReservationResponse;
import fib.br10.service.abstracts.CustomerHistoryService;
import fib.br10.service.abstracts.ReservationService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerHistoryServiceImpl implements CustomerHistoryService {

    ReservationService reservationService;

    @Override
    public List<CustomerHistoryResponse> getCustomerHistories(Long customerId) {
        if (reservationService == null) {
            return Collections.emptyList();
        }

        List<ReservationResponse> reservations = reservationService.findAllByCustomerId(customerId);

        if (reservations == null || reservations.isEmpty()) {
            return Collections.emptyList();
        }

        return reservations.stream()
                .filter(Objects::nonNull)
                .map(reservationResponse ->
                        new CustomerHistoryResponse(
                                reservationResponse.getId() != null ? reservationResponse.getId() : -1L // Default ID if null
                        )
                )
                .toList();
    }

}