package fib.br10.controller;

import fib.br10.configuration.SecurityEnv;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Security;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
@Validated
public class EnvironmentController {

    private final SecurityEnv securityEnv;

    @GetMapping("/allowed-origins")
    public List<String> getAllowedOrigins() {
        return securityEnv.getCorsAllowedOrigins();
    }
}
