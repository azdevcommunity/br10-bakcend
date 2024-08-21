package fib.br10.entity.specialist;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

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
@Table(name = "SPECIALIST_BLOCKED_CUSTOMERS")
@EntityListeners(BaseEntityListener.class)
public class SpecialistBlockedCustomer extends BaseEntity {

        @Column(name = "SPECIALIST_USER_ID", nullable = false)
        Long specialistUserId;

        @Column(name = "CUSTOMER_USER_ID", nullable = false)
        Long customerUserId;

        @Column(name = "REASON", length = 100)
        String reason;
}
