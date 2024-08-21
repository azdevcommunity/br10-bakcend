package fib.br10.controller;

import fib.br10.core.service.RequestContextProvider;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/block")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> blockCustomer(@RequestBody @Valid BlockCustomerRequest request) {
        return ResponseEntity.ok(specialistBlockedCustomerService.blockCustomer(request, provider.getUserId()));
    }

    @PostMapping("/get-blocked-customers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialistBlockedCustomerResponse>> getBlockedCustomers() {
        return ResponseEntity.ok(specialistBlockedCustomerService.getBlockedCustomers(provider.getUserId()));
    }

    @PostMapping("/unblock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> unblockCustomer(@RequestBody @Valid UnBlockCustomerRequest request) {
        return ResponseEntity.ok(specialistBlockedCustomerService.unblockCustomer(request, provider.getUserId()));
    }
}

