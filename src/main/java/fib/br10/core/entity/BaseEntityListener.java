package fib.br10.core.entity;

import fib.br10.core.utility.DateUtil;
import fib.br10.core.utility.RequestContext;
import fib.br10.core.utility.RequestContextEnum;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Configurable;

import org.springframework.beans.factory.aspectj.ConfigurableObject;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;


@Configurable
public class BaseEntityListener implements ConfigurableObject {

    @PrePersist
    public void prePersist(BaseEntity entity) {
        final UUID activityId = UUID.fromString(RequestContext.get(RequestContextEnum.ACTIVITY_ID));
        final Long currentUserId = RequestContext.get(RequestContextEnum.USER_ID, Long.class);

        entity.setCreatedDate(DateUtil.getCurrentDateTime());

        if (Objects.isNull(entity.getStatus())) {
            entity.setStatus(EntityStatus.ACTIVE.getValue());
        }
        if (Objects.isNull(entity.getActivityId())) {
            entity.setActivityId(activityId);
        }
        if (Objects.isNull(entity.getCreatedBy()) && Objects.nonNull(currentUserId)) {
            entity.setCreatedBy(currentUserId.toString());
        }
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        final Long currentUserId = RequestContext.get(RequestContextEnum.USER_ID, Long.class);

        entity.setLastModifiedDate(DateUtil.getCurrentDateTime());

        if (Objects.nonNull(currentUserId)) {
            entity.setLastModifiedBy(currentUserId.toString());
        }
    }

}
