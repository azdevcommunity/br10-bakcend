package fib.br10.core.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@SuperBuilder
@EntityListeners({BaseEntityListener.class})
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    Long id;

    @Column(name = "STATUS", nullable = false)
    Integer status;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    OffsetDateTime lastModifiedDate;

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false)
    OffsetDateTime createdDate;

    @CreatedBy
    @Column(name = "CREATED_BY")
    String createdBy;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    String lastModifiedBy;

    @LastModifiedBy
    @Column(name = "DELETED_BY")
    String deletedBy;

    @Column(name = "ACTIVITY_ID", nullable = false)
    UUID activityId;
}
