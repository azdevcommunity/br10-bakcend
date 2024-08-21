package fib.br10.repository;

import fib.br10.dto.reservation.response.ReservationResponse;
import fib.br10.entity.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>,
        QuerydslPredicateExecutor<Reservation> {

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
            FROM Reservation r
            WHERE r.specialistUserId = :specialistUserId
            AND r.reservationStatus = :reservationStatus
            AND r.reservationDate BETWEEN :startDate AND :endDate
            """)
    boolean reservationExists(Long specialistUserId, Integer reservationStatus, OffsetDateTime startDate, OffsetDateTime endDate);

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
            FROM Reservation r
            WHERE r.specialistUserId = :specialistUserId
            AND r.reservationStatus = :reservationStatus
            AND r.reservationDate BETWEEN :startDate AND :endDate
            AND r.id <> :id
            """)
    boolean reservationExists(Long specialistUserId, Integer reservationStatus, OffsetDateTime startDate, OffsetDateTime endDate, Long id);


    @Query("""
                select new fib.br10.dto.reservation.response.ReservationResponse(
                 r.id, r.createdDate, r.reservationDate, r.specialistUserId, su.username, cu.id, cu.username,
                                        cu.phoneNumber, ss.id, ss.price, r.reservationSource, r.reservationStatus, ss.duration)
                from Reservation r
                left join User su on r.specialistUserId =  su.id
                left join User cu on r.customerUserId = cu.id
                left join SpecialistService ss on r.specialistServiceId  = ss.id
                where r.specialistUserId =:userId  and r.status =:entityStatus and r.reservationStatus =:reservationStatus
                and r.reservationDate between :startOfDay and :endOfDay
            """)
    List<ReservationResponse> findAllPendingReservations(Long userId,
                                                         OffsetDateTime startOfDay,
                                                         OffsetDateTime endOfDay,
                                                         int entityStatus,
                                                         int reservationStatus);

    boolean findByCustomerUserIdAndReservationStatusAndStatus(Long userId, Integer reservStatus,Integer status);
}