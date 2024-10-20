package fib.br10.service.abstracts;

import fib.br10.dto.customer.response.ReadReservationsResponse;
import fib.br10.dto.specialist.specialistprofile.request.BlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.request.UnBlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistBlockedCustomerResponse;
import fib.br10.entity.specialist.SpecialistBlockedCustomer;

import java.time.LocalDateTime;
import java.util.List;

public interface SpecialistCustomerService {
    SpecialistBlockedCustomerResponse unblockCustomer(UnBlockCustomerRequest request, Long userId);

    List<SpecialistBlockedCustomerResponse> getBlockedCustomers(Long userId);

    SpecialistBlockedCustomerResponse blockCustomer(BlockCustomerRequest request, Long userId);

    SpecialistBlockedCustomer findByCustomerUserIdAndSpecialistId(Long customerUserId, Long specialistUserId);


    List<ReadReservationsResponse> findAllReservations(Long pageSize, Long pageNumber, LocalDateTime reservationDate);

    void checkIsCustomerBlocked(Long specialistUserId, Long customerUserId);

}
