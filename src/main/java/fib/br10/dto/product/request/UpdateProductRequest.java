package fib.br10.dto.product.request;

import fib.br10.utility.Messages;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProductRequest {
    @NotNull(message = Messages.ID_REQUIRED)
    Long id;
    @NotBlank
    @Column(name = "NAME", nullable = false, length = 100)
    String name;

    String description;

    BigDecimal price;

    Long categoryId;
}
