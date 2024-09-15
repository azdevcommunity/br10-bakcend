package fib.br10.dto.specialist.specialistprofile.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadByPhoneNumbersRequest {
    private List<String> phoneNumbers;
}
