package fib.br10.controller;

import fib.br10.core.dto.RequestById;
import fib.br10.dto.specialist.specialistprofile.request.UpdateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse;
import fib.br10.service.SpecialistProfileService;
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


import static fib.br10.core.utility.RequestContext.get;
import static fib.br10.core.utility.RequestContextEnum.USER_ID;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/specialist-profile")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@PreAuthorize("hasRole('SPECIALIST')")
public class SpecialistProfileController {

    SpecialistProfileService specialistProfileService;

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialistProfileReadResponse> read(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(specialistProfileService.read(request));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> update(
            @RequestPart("profilePicture") @Nullable MultipartFile profilePicture,
            @ModelAttribute @Valid UpdateSpecialistProfileRequest request) {
        return ResponseEntity.ok(specialistProfileService.update(request, profilePicture, get(USER_ID)));
    }
}