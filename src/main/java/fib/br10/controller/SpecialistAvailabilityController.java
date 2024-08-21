package fib.br10.controller;

import fib.br10.dto.specialist.specialistavailability.request.CreateSpecialistAvailabilityRequest;
import fib.br10.dto.specialist.specialistavailability.response.SpecialistAvailabilityReadResponse;
import fib.br10.service.SpecialistAvailabilityService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/specialist-availability")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('SPECIALIST')")
public class SpecialistAvailabilityController {

   SpecialistAvailabilityService specialistAvailabilityService;


    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSpecialistAvailabilityRequest request) {
        return ResponseEntity.ok(specialistAvailabilityService.crate(request));
    }

    @PostMapping("/read")
    public ResponseEntity<List<SpecialistAvailabilityReadResponse>> read() {
        return ResponseEntity.ok(specialistAvailabilityService.read());
    }
}