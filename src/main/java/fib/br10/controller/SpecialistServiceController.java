package fib.br10.controller;

import fib.br10.core.dto.RequestById;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.specialist.specialistservice.request.CreateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.request.UpdateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.response.SpecialistServiceResponse;
import fib.br10.service.SpecialistServiceManager;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/specialist-service")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('SPECIALIST')")
public class SpecialistServiceController {
    SpecialistServiceManager specialistServiceManager;
    RequestContextProvider provider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SpecialistServiceResponse> create(@RequestPart("image") @Nullable MultipartFile image,
                                       @ModelAttribute @Valid CreateSpecialistServiceRequest request) {
        request.setImage(image);
        return ResponseEntity.ok(specialistServiceManager.create(request, provider.getUserId()));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistServiceResponse> update(@RequestBody @Valid UpdateSpecialistServiceRequest request) {
        return ResponseEntity.ok(specialistServiceManager.update(request, provider.getUserId()));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistServiceResponse> update(@RequestPart("image") @Nullable MultipartFile image,
                                                            @PathVariable("id") @Valid Long id) {
        return ResponseEntity.ok(specialistServiceManager.update(image, id, provider.getUserId()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> delete(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(specialistServiceManager.delete(request, provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/specialist/{specialistId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialistServiceResponse>> getAllById(@PathVariable("specialistId") @Valid Long specialistId) {
        return ResponseEntity.ok(specialistServiceManager.findAllSpecialistServices(specialistId));
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialistServiceResponse>> getAll() {
        return ResponseEntity.ok(specialistServiceManager.findAllSpecialistServices(provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistServiceResponse> getById(@PathVariable("id") @Valid Long id) {
        return ResponseEntity.ok(specialistServiceManager.findServiceById(id));
    }
}
