package fib.br10.dto.specialist.specialistservice.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class SpecialistServiceResponse {
    Long id;

    Long specialistUserId;

    Integer duration;

    String name;

    BigDecimal price;

    String description;

    String image;
}
