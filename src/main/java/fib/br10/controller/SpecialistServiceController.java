package fib.br10.controller;

import fib.br10.core.dto.RequestById;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.specialist.specialistservice.request.CreateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.request.GetSpecialistServicesRequest;
import fib.br10.dto.specialist.specialistservice.request.UpdateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.response.ReadSpecialistServiceResponse;
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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> create(@RequestPart("image") @Nullable MultipartFile image,
                                       @ModelAttribute @Valid CreateSpecialistServiceRequest request) {
        request.setImage(image);
        return ResponseEntity.ok(specialistServiceManager.create(request, provider.getUserId()));
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> update(@RequestPart("image") @Nullable MultipartFile image,
                                       @ModelAttribute @Valid UpdateSpecialistServiceRequest request) {
        request.setImage(image);
        return ResponseEntity.ok(specialistServiceManager.update(request, provider.getUserId()));
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> update(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(specialistServiceManager.delete(request, provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/read")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ReadSpecialistServiceResponse>> getAll(@RequestBody @Valid GetSpecialistServicesRequest request) {
        return ResponseEntity.ok(specialistServiceManager.findAllSpecialistServices(request));
    }
}
