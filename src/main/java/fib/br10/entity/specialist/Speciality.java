package fib.br10.entity.specialist;

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
@Table(name = "SPECIALITIES")
@EntityListeners(BaseEntityListener.class)
public class Speciality extends BaseEntity {
    @Column(name = "NAME",nullable = false)
    String name;
    @Column(name = "NAME_EN",nullable = false)
    String name_en;
    @Column(name = "NAME_RU",nullable = false)
    String name_ru;
}
