package fib.br10.repository;

import fib.br10.entity.reservation.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail,Long> {
    List<ReservationDetail> findByReservationId(Long reservationId);
}
