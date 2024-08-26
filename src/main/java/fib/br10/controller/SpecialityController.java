package fib.br10.controller;

import fib.br10.core.dto.RequestById;
import fib.br10.dto.speciality.request.CreateSpecialityRequest;
import fib.br10.dto.speciality.request.UpdateSpecialityRequest;
import fib.br10.dto.speciality.response.SpecialityResponse;
import fib.br10.service.SpecialityService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/specialities")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasRole('ADMIN')")
public class SpecialityController {
    SpecialityService specialityService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSpecialityRequest request) {
        return ResponseEntity.ok(specialityService.create(request));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> delete(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(specialityService.delete(request));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> update(@RequestBody @Valid UpdateSpecialityRequest request) {
        return ResponseEntity.ok(specialityService.update(request));
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialityResponse>> findAll() {
        return ResponseEntity.ok(specialityService.findAll());
    }
}
