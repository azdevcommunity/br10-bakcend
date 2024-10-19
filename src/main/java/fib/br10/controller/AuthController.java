package fib.br10.controller;

import fib.br10.core.dto.Token;
import fib.br10.dto.auth.request.*;
import fib.br10.dto.auth.response.OtpResponse;
import fib.br10.dto.auth.response.RegisterResponse;
import fib.br10.service.AuthService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController{

    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Token> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/activate-user-verify-otp")
    public ResponseEntity<Token> activateUserVerifyOtp(@RequestBody @Valid ActivateUserVerifyOtpRequest request) {
        return ResponseEntity.ok(authService.activateUserVerifyOtp(request));
    }

    @PostMapping("/get-otp")
    public ResponseEntity<OtpResponse> getOtp(@RequestBody @Valid GetOtpRequest request) {
        return ResponseEntity.ok(authService.getOtp(request));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Token> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password-verify-otp")
    public ResponseEntity<String> resetPasswordVerifyOtp(@RequestBody @Valid VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.resetPasswordVerifyOtp(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Long> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}



