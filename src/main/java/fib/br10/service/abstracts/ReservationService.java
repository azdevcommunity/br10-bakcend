package fib.br10.service.abstracts;

import com.querydsl.core.BooleanBuilder;
import fib.br10.dto.reservation.request.CancelReservationRequest;
import fib.br10.dto.reservation.request.CreateReservationRequest;
import fib.br10.dto.reservation.request.UpdateReservationRequest;
import fib.br10.dto.reservation.response.ReservationResponse;
import fib.br10.entity.reservation.*;

import java.util.List;

public interface ReservationService {
    ReservationResponse updateReservation(UpdateReservationRequest request);

    ReservationResponse createReservation(CreateReservationRequest request);

    Long cancelReservation(CancelReservationRequest request);

    List<ReservationResponse> findAllReservations();

    Reservation findOne(BooleanBuilder predicate);

    Reservation findById(Long id);
}
