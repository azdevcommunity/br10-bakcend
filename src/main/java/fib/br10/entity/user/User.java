package fib.br10.entity.user;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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
@Table(name = "USERS")
@EntityListeners(BaseEntityListener.class)
public class User extends BaseEntity {

    @Column(name = "NAME", length = 100)
    String name;

    @Column(name = "SURNAME", length = 100)
    String surname;

    @Column(name = "USERNAME", length = 100)
    String username;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 100)
    String phoneNumber;

    @Column(name = "PASSWORD", length = 100, nullable = false)
    String password;

    @Column(name = "EMAIL", length = 100)
    String email;

    @Column(name = "BIRTH_DAY")
    OffsetDateTime birthDay;
}
