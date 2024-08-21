package fib.br10.dto.image.response;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NotNull
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateImageResponse {
    Long id;
    String name;
    String path;
    String extension;
}
