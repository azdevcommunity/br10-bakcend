package fib.br10.dto.reservation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import fib.br10.entity.reservation.ReservationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ReservationResponse {

    public ReservationResponse(Long id) {
        this.id = id;
    }

    Long id;

    OffsetDateTime createdDate;

    OffsetDateTime reservationDate;

    Long specialistUserId;

    String specialistUsername;

    Long customerUserId;

    String customerUsername;

    String customerUserPhoneNumber;

//    Long specialistServiceId;

    BigDecimal price;

    Integer reservationSource;

    Integer reservationStatus;

    Integer duration;

    List<ReservationDetailResponse> reservationDetail;

    public ReservationResponse(Long id, OffsetDateTime createdDate, OffsetDateTime reservationDate, Long specialistUserId, String specialistUsername, Long customerUserId, String customerUsername, String customerUserPhoneNumber, BigDecimal price, Integer reservationSource, Integer reservationStatus, Integer duration) {
        this.id = id;
        this.createdDate = createdDate;
        this.reservationDate = reservationDate;
        this.specialistUserId = specialistUserId;
        this.specialistUsername = specialistUsername;
        this.customerUserId = customerUserId;
        this.customerUsername = customerUsername;
        this.customerUserPhoneNumber = customerUserPhoneNumber;
        this.price = price;
        this.reservationSource = reservationSource;
        this.reservationStatus = reservationStatus;
        this.duration = duration;
    }
}
