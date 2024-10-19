package fib.br10.controller;

import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.customer.response.ReadReservationsResponse;
import fib.br10.dto.specialist.specialistprofile.request.BlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.request.UnBlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistBlockedCustomerResponse;
import fib.br10.service.SpecialistBlockedCustomerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/specialist-customer")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('SPECIALIST')")
public class SpecialistBlockedCustomerController {
    SpecialistBlockedCustomerService specialistBlockedCustomerService;
    RequestContextProvider provider;

    @PutMapping("/block")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistBlockedCustomerResponse> blockCustomer(@RequestBody @Valid BlockCustomerRequest request) {
        return ResponseEntity.ok(specialistBlockedCustomerService.blockCustomer(request, provider.getUserId()));
    }

    @GetMapping("/blocked-customers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialistBlockedCustomerResponse>> getBlockedCustomers() {
        return ResponseEntity.ok(specialistBlockedCustomerService.getBlockedCustomers(provider.getUserId()));
    }

    @PutMapping("/unblock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistBlockedCustomerResponse> unblockCustomer(@RequestBody @Valid UnBlockCustomerRequest request) {
        return ResponseEntity.ok(specialistBlockedCustomerService.unblockCustomer(request, provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/reservations")
    public ResponseEntity<List<ReadReservationsResponse>> findReservations(
            @RequestParam(value = "pageSize", required = false) Long pageSize,
            @RequestParam(value = "pageNumber", required = false) Long pageNumber,
            @RequestParam(value = "reservationDate", required = false) LocalDateTime reservationDate) {
        return ResponseEntity.ok(specialistBlockedCustomerService.findAllReservations(pageSize, pageNumber, reservationDate));
    }
}

