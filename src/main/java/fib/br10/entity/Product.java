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

import java.math.BigDecimal;

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
@Table(name = "PRODUCTS")
public class Product extends BaseEntity {

    @Column(name = "NAME", nullable = false, length = 100)
    String name;

    @Column(name = "DESCRIPTION", length = 500)
    String description;

    @Column(name = "PRICE")
    BigDecimal price;

    @Column(name = "SPECIALIST_USER_ID", nullable = false)
    Long specialistUserId;

    @Column(name = "CATEGORY_ID")
    Long categoryId;

    @Column(name = "IMAGE_ID")
    Long imageId;
}
