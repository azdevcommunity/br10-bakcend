package fib.br10.core.dto;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestById {

    @NotNull(message = Messages.ID_REQUIRED)
    Long id;

}


