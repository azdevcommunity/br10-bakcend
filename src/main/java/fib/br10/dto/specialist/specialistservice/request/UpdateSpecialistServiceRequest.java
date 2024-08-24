package fib.br10.dto.specialist.specialistservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSpecialistServiceRequest {
    @NotNull
    Long id;

    @NotNull
    Integer duration;

    @NotNull
    String name;

    @NotNull
    BigDecimal price;

    String description;

    MultipartFile image;
}
