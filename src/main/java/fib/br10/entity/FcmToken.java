package fib.br10.entity;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "FCM_TOKENS")
@EntityListeners(BaseEntityListener.class)
public class FcmToken extends BaseEntity {

    @Column(nullable = false, name = "DEVICE_ID")
    Long deviceId;

    @Column(nullable = false, name = "USER_ID")
    Long userId;

    @Column(nullable = false, unique = true,name = "TOKEN")
    String token;
}
