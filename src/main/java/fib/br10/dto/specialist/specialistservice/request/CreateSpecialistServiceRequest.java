package fib.br10.dto.specialist.specialistservice.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
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
public class CreateSpecialistServiceRequest {

    @NotNull
    Integer duration;

    @NotBlank(message = Messages.SPECIALIST_SERVICE_NAME_REQUIRED)
    String name;

    @NotNull(message = Messages.SPECIALIST_SERVICE_PRICE_REQUIRED)
    BigDecimal price;

    MultipartFile image;
}
