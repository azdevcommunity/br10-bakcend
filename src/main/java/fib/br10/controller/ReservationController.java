package fib.br10.controller;

import fib.br10.core.dto.RequestById;
import fib.br10.dto.reservation.request.CancelReservationRequest;
import fib.br10.dto.reservation.request.CreateReservationRequest;
import fib.br10.dto.reservation.request.UpdateReservationRequest;
import fib.br10.dto.reservation.response.ReservationResponse;
import jakarta.validation.Valid;
import fib.br10.service.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<ReservationResponse>> findAll(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(reservationService.findAllReservations(request));
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
