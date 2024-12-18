package fib.br10.controller;

import fib.br10.core.dto.Token;
import fib.br10.dto.auth.request.ActivateUserVerifyOtpRequest;
import fib.br10.dto.auth.request.GetOtpRequest;
import fib.br10.dto.auth.request.LoginRequest;
import fib.br10.dto.auth.request.RefreshTokenRequest;
import fib.br10.dto.auth.request.RegisterRequest;
import fib.br10.dto.auth.request.ResetPasswordRequest;
import fib.br10.dto.auth.request.VerifyOtpRequest;
import fib.br10.dto.auth.response.OtpResponse;
import fib.br10.dto.auth.response.RegisterResponse;
import fib.br10.entity.user.User;
import fib.br10.service.AuthService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Token> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                              @RequestBody @Nullable RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request, refreshToken));
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
    public ResponseEntity<Token> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
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


    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(@CookieValue(value = "accessToken", required = false) String accessToken) {
        User user = authService.validateToken(accessToken);
        return ResponseEntity.ok(user);
    }

}



