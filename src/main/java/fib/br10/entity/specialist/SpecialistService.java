package fib.br10.entity.specialist;

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
@Table(name = "SPECIALIST_SERVICES")
@EntityListeners(BaseEntityListener.class)
public class SpecialistService extends BaseEntity {

    @Column(name = "SPECIALIST_USER_ID", nullable = false)
    Long specialistUserId;

    @Column(name = "DURATION", nullable = false)
    Integer duration;

    @Column(name = "NAME", length = 100, nullable = false)
    String name;

    @Column(name = "PRICE", nullable = false)
    BigDecimal price;

    @Column(name = "IMAGE_ID")
    Long imageId;

    @Column(name = "DESCRIPTION", length = 500)
    String description;
}