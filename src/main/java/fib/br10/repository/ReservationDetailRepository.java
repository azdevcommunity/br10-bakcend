package fib.br10.repository;

import fib.br10.dto.reservation.response.ReservationDetailResponse;
import fib.br10.entity.reservation.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail,Long> {
    List<ReservationDetail> findByReservationId(Long reservationId);

    @Query("""
                    select new fib.br10.dto.reservation.response.ReservationDetailResponse(
                    r.id,
                    r.reservationId,
                    r.serviceId,
                    r.duration,
                    r.price,
                    ss.name )
                    from ReservationDetail r
                    left join SpecialistService  ss on r.serviceId = ss.id
                    where r.reservationId in :reservationIds
            """)
    List<ReservationDetailResponse> findAllReservationDetails(List<Long> reservationIds);
}
