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
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "SPECIALIST_PROFILES")
@EntityListeners(BaseEntityListener.class)
public class SpecialistProfile extends BaseEntity {

    @Column (name="SPECIALIST_USER_ID", nullable = false, unique = true)
    Long specialistUserId;

    @Column(name = "SPECIALITY_ID", nullable = false)
    Long specialityId;

    @Column(name = "ADDRESS", length = 100)
    String address;

    @Column(name = "CITY", length = 100)
    String city;

    @Column(name = "INSTAGRAM", length = 100)
    String instagram;

    @Column(name = "TIKTOK", length = 100)
    String tiktok;

    @Column(name = "FACEBOOK", length = 100)
    String facebook;

    @Column(name = "IMAGE_ID")
    Long imageId;
}