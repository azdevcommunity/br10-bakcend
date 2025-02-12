package fib.br10.service.abstracts;

import fib.br10.dto.specialist.favoritespecialist.response.FavoriteSpecialistResponse;
import jakarta.validation.Valid;

public interface FavoriteSpecialistService {
    FavoriteSpecialistResponse create(Long specialistId);

    void delete(@Valid Long specialistId);
}
