package fib.br10.entity.specialist;

import fib.br10.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;
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
@Table(name = "SPECIALIST_AVAILABILITIES")
public class SpecialistAvailability extends BaseEntity {

    @Column(name = "SPECIALIST_USER_ID", nullable = false)
    Long specialistUserId;

    @Column(name = "DAY_OF_WEEK", nullable = false)
    Integer dayOfWeek;

    @Column(name = "START_TIME")
    OffsetDateTime startTime;

    @Column(name = "END_TIME")
    OffsetDateTime endTime;

    @Column(name = "IS_WEEKEND")
    Boolean isWeekend;
}