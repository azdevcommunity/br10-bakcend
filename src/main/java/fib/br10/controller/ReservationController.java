package fib.br10.controller;

import fib.br10.dto.reservation.request.CancelReservationRequest;
import fib.br10.dto.reservation.request.CreateReservationRequest;
import fib.br10.dto.reservation.request.UpdateReservationRequest;
import fib.br10.dto.reservation.response.ReservationResponse;
import fib.br10.service.abstracts.ReservationService;
import jakarta.validation.Valid;
import fib.br10.service.ReservationServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Validated
public class ReservationController {

    ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid CreateReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('SPECIALIST')")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @PutMapping("/cancel")
    public ResponseEntity<Long> cancel(@RequestBody @Valid CancelReservationRequest request) {
        return ResponseEntity.ok(reservationService.cancelReservation(request));
    }

    @PutMapping
    public ResponseEntity<ReservationResponse> update(@RequestBody @Valid UpdateReservationRequest request) {
        return ResponseEntity.ok(reservationService.updateReservation(request));
    }
}
