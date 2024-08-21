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
@Table(name = "IMAGES")
@EntityListeners(BaseEntityListener.class)
public class Image extends BaseEntity {

    @Column(name = "NAME", length = 100)
    String name;

    @Column(name = "PATH", length = 500)
    String path;

    @Column(name = "EXTENSION", length = 100)
    String extension;
}
