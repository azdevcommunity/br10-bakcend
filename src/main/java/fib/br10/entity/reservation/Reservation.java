package fib.br10.entity.reservation;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "RESERVATIONS")
@EntityListeners(BaseEntityListener.class)
public class Reservation extends BaseEntity {
    @Column(name = "RESERVATION_DATE", nullable = false)
    OffsetDateTime reservationDate;

    @Column(name = "SPECIALIST_USER_ID", nullable = false)
    Long specialistUserId;

    @Column(name = "CUSTOMER_USER_ID")
    Long customerUserId;

    @Column(name = "SPECIALIST_SERVICE_ID", nullable = false)
    Long specialistServiceId;

    @Column(name = "PRICE", nullable = false)
    BigDecimal price;

    @Column(name = "RESERVATION_SOURCE", nullable = false)
    Integer reservationSource;

    @Column(name = "RESERVATION_STATUS", nullable = false)
    Integer reservationStatus;

    @Column(name = "DURATION", nullable = false)
    Integer duration;
}
