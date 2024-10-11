package fib.br10.entity;

import fib.br10.core.entity.BaseEntity;
import fib.br10.core.entity.BaseEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
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
@Table(name = "NOTIFICATION")
@EntityListeners(BaseEntityListener.class)
public class Notification extends BaseEntity {

    @Column(name = "TITLE")
    String title;

    @Column(name = "BODY")
    String body;

    @Column(name = "USER_ID", nullable = false)
    Long userId;
}
