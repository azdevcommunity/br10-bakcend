package fib.br10.entity;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@EntityListeners(BaseEntityListener.class)
@Table(name = "OTP", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_ID", "CODE"})
})
public class Otp extends BaseEntity {

    @Column(name = "USER_ID",nullable = false)
    Long userId;

    @Column(name = "CODE",nullable = false)
    Integer code;

    @Column(name = "EXPIRE_DATE",nullable = false)
    OffsetDateTime expireDate;

    @Column(name = "COUNT")
    Integer count;

    @Column(name = "VERIFIED")
    Boolean verified;
}
