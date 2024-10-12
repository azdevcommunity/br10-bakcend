package fib.br10.entity.reservation;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "RESERVATION_DETAILS")
@EntityListeners(BaseEntityListener.class)
public class ReservationDetail extends BaseEntity {
    @Column(name = "RESERVATION_ID")
    Long reservationId;
    @Column(name = "SERVICE_ID")
    Long serviceId;
    @Column(name = "DURATION")
    Integer duration;
    @Column(name ="PRICE")
    BigDecimal price;
}
