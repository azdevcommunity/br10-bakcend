package fib.br10.dto.history.customer.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;


public interface CustomerHistoryDetailsProjection {
    Long getReservationId();
    OffsetDateTime getReservationDate();
    Integer getReservationStatus();
    String getSpecialistName();
    Long getSpecialistId();
    BigDecimal getPrice();
    List<ServiceResponseProjection> getServices();
}

 interface ServiceResponseProjection {
    Long getId();
    Long getSpecialistUserId();
    Integer getDuration();
    String getName();
    BigDecimal getPrice();
    String getDescription();
    String getImage();
}