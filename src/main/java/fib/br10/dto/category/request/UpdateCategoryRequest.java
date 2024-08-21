package fib.br10.dto.category.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCategoryRequest {

    @NotBlank(message = Messages.ID_REQUIRED)
    Long id;

    @NotBlank(message = Messages.CATEGORY_NAME_REQUIRED)
    String name;

    String description;

}
