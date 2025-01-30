package fib.br10.service.abstracts;

import fib.br10.dto.history.customer.response.CustomerHistoryResponse;
import java.util.List;

public interface CustomerHistoryService {
    List<CustomerHistoryResponse> getCustomerHistories(Long customerId);
}