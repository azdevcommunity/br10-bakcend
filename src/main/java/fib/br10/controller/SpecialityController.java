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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/specialities")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class SpecialityController {
    SpecialityService specialityService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSpecialityRequest request) {
        return ResponseEntity.ok(specialityService.create(request));
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> delete(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(specialityService.delete(request));
    }
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> update(@RequestBody @Valid UpdateSpecialityRequest request) {
        return ResponseEntity.ok(specialityService.update(request));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/read")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialityResponse>> findAll() {
        return ResponseEntity.ok(specialityService.findAll());
    }
}
