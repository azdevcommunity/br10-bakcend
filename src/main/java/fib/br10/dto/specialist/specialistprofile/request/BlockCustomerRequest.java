package fib.br10.dto.specialist.specialistprofile.request;

import fib.br10.utility.Messages;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockCustomerRequest {

    @NotNull(message = Messages.CUSTOMER_USER_ID_REQUIRED)
    Long customerUserId;


    String reason;
}
