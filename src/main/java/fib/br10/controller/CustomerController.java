package fib.br10.controller;

import fib.br10.dto.history.customer.response.CustomerHistoryDetailsProjection;
import fib.br10.dto.history.customer.response.CustomerHistoryResponse;
import fib.br10.service.abstracts.CustomerHistoryService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    CustomerHistoryService customerHistoryService;

    @GetMapping("/history")
    public List<CustomerHistoryResponse> getCustomerHistory() {
        return customerHistoryService.getCustomerHistories();

    }
    @GetMapping("/history/id")
    public List<CustomerHistoryDetailsProjection> getCustomerHistory(Long reservationId) {
        return customerHistoryService.getCustomerHistoryByReservation(reservationId);
    }

}