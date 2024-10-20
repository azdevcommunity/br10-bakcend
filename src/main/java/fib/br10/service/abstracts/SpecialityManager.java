package fib.br10.service.abstracts;

import fib.br10.core.dto.RequestById;
import fib.br10.dto.speciality.request.CreateSpecialityRequest;
import fib.br10.dto.speciality.request.UpdateSpecialityRequest;
import fib.br10.dto.speciality.response.SpecialityResponse;

import java.util.List;

public interface SpecialityManager {
    Long create(CreateSpecialityRequest request);

    Long update(UpdateSpecialityRequest request);

    Long delete(RequestById request);

    List<SpecialityResponse> findAll();

    void checkSpecialityExists(Long specialityId);

    String findSpecialistyName(Long id);
}
