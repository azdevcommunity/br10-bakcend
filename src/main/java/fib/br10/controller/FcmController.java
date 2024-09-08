package fib.br10.controller;

import fib.br10.dto.fcmtoken.request.CreateFcmTokenRequest;
import fib.br10.service.FcmTokenService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/fcm")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('SPECIALIST')")
public class FcmController {

    FcmTokenService fcmTokenService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateFcmTokenRequest request) {
        fcmTokenService.create(request);
        return ResponseEntity.ok().build();
    }
}
