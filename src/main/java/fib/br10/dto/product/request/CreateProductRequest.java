package fib.br10.dto.product.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductRequest {
    @NotBlank
    String name;

    String description;

    BigDecimal price;

    Long categoryId;

    MultipartFile image;
}
