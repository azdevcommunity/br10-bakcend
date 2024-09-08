package fib.br10.entity.user;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "USER_DEVICES")
@EntityListeners(BaseEntityListener.class)
public class UserDevice extends BaseEntity {

    @Column(name = "DEVICE_ID", nullable = false)
    private String deviceId;

    @Column(name = "CLIENT_TYPE", nullable = false)
    private Integer clientType;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "OPERATING_SYSTEM", length = 50)
    private String operatingSystem;

    @Column(name = "OS_VERSION", length = 50)
    private String osVersion;

    @Column(name = "APP_VERSION", length = 50)
    private String appVersion;

    @Column(name = "BRAND", length = 50)
    private String brand;

    @Column(name = "MODEL", length = 50)
    private String model;
}
