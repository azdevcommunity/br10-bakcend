package fib.br10.service.abstracts;

import fib.br10.dto.specialist.specialistavailability.request.CreateSpecialistAvailabilityRequest;
import fib.br10.dto.specialist.specialistavailability.response.SpecialistAvailabilityReadResponse;


import java.util.List;

public interface SpecialistAvailabilityService {

    List<SpecialistAvailabilityReadResponse> read();

    Long crate(CreateSpecialistAvailabilityRequest request);

    void addWeekendAvailability(Long specialistUserId);
}
