package fib.br10.core.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Token {
    String accessToken;
    String refreshToken;
}
