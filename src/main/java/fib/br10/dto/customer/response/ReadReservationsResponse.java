package fib.br10.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class ReadReservationsResponse {
    Long id;

    Long detailId;

    OffsetDateTime reservationDate;

    Long specialistId;

    String specialistUserName;

    Long customerUserId;

    BigDecimal price;

    Integer reservationSource;

    Integer reservationStatus;

    Integer duration;

    List<ReadReservationsResponse> services;

    public ReadReservationsResponse(Long id, Long detailId, OffsetDateTime reservationDate, Long specialistId, String specialistUserName, Long customerUserId, BigDecimal price, Integer reservationSource, Integer reservationStatus, Integer duration) {
        this.id = id;
        this.detailId = detailId;
        this.reservationDate = reservationDate;
        this.specialistId = specialistId;
        this.specialistUserName = specialistUserName;
        this.customerUserId = customerUserId;
        this.price = price;
        this.reservationSource = reservationSource;
        this.reservationStatus = reservationStatus;
        this.duration = duration;
    }
}
