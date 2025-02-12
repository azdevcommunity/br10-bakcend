package fib.br10.service;

import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.history.customer.response.CustomerHistoryDetailsDTO;
import fib.br10.dto.history.customer.response.CustomerHistoryResponse;
import fib.br10.service.abstracts.CustomerHistoryService;
import fib.br10.service.abstracts.ReservationService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerHistoryServiceImpl implements CustomerHistoryService {

    ReservationService reservationService;
    RequestContextProvider provider;

    @Override
    public List<CustomerHistoryResponse> getCustomerHistories() {
        Long userId = provider.getUserId();
        List<CustomerHistoryResponse> reservations = reservationService.findHistory(userId);

        return reservations;
    }

    @Override
    public List<CustomerHistoryDetailsDTO> getCustomerHistoryByReservation(long reservationId) {
        return reservationService.getCustomerHistoryByReservation(reservationId);
    }

}