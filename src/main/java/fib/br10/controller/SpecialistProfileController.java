package fib.br10.controller;

import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.specialist.specialistprofile.request.ReadByPhoneNumbersRequest;
import fib.br10.dto.specialist.specialistprofile.request.UpdateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse;
import fib.br10.service.SpecialistProfileService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

import static fib.br10.core.utility.RequestContext.get;
import static fib.br10.core.utility.RequestContextEnum.USER_ID;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/specialist-profile")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('SPECIALIST')")
public class SpecialistProfileController {

    SpecialistProfileService specialistProfileService;
    RequestContextProvider provider;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistProfileReadResponse> read() {
        return ResponseEntity.ok(specialistProfileService.read(provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/specialist/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistProfileReadResponse> read(@PathVariable("id") @NotNull Long id) {
        return ResponseEntity.ok(specialistProfileService.read(id));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/by-numbers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialistProfileReadResponse>> read(@RequestBody @Valid  ReadByPhoneNumbersRequest request) {
        return ResponseEntity.ok(specialistProfileService.read(request));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/by-number/{phoneNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistProfileReadResponse> read(@PathVariable("phoneNumber") @NotNull  String phoneNumber) {
        return ResponseEntity.ok(specialistProfileService.read(phoneNumber));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistProfileReadResponse> update(@RequestBody @Valid UpdateSpecialistProfileRequest request) {
        return ResponseEntity.ok(specialistProfileService.update(request, get(USER_ID)));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistProfileReadResponse> update(@RequestPart("profilePicture") @Nullable MultipartFile profilePicture) {
        return ResponseEntity.ok(specialistProfileService.update(profilePicture, get(USER_ID)));
    }
}