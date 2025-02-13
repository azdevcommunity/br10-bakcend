package fib.br10.service;

import fib.br10.core.exception.BaseException;
import fib.br10.core.exception.NotFoundException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.specialist.favoritespecialist.response.FavoriteSpecialistResponse;
import fib.br10.entity.specialist.FavoriteSpecialist;
import fib.br10.mapper.FavoriteSpecialistMapper;
import fib.br10.repository.FavoriteSpecialistRepository;
import fib.br10.service.abstracts.FavoriteSpecialistService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteSpecialistServiceImpl implements FavoriteSpecialistService {

    final FavoriteSpecialistRepository favoriteSpecialistRepository;
    final FavoriteSpecialistMapper favoriteSpecialistMapper;
    final RequestContextProvider provider;
    final UserService userService;

    @Override
    public FavoriteSpecialistResponse create(Long specialistId) {
        Long userId = provider.getUserId();

        userService.existsByIdAndUserRoleSpecialist(specialistId);

        FavoriteSpecialist favoriteSpecialist = FavoriteSpecialist.builder()
                .specialistId(specialistId)
                .userId(userId)
                .build();

        favoriteSpecialistRepository.save(favoriteSpecialist);

        return favoriteSpecialistMapper.toResponse(favoriteSpecialist);
    }

    @Override
    public void delete(Long specialistId) {
        FavoriteSpecialist favoriteSpecialist = favoriteSpecialistRepository.findById(specialistId)
                .orElseThrow(() -> new NotFoundException("Favorite Specialist Not Found"));

        if (!favoriteSpecialist.getUserId().equals(provider.getUserId())) {
            throw new BaseException(HttpStatus.UNAUTHORIZED, "User is not authorized to delete special favorite specialist");
        }

        favoriteSpecialistRepository.delete(favoriteSpecialist);
    }

    @Override
    public List<FavoriteSpecialistResponse> findAll() {
        return favoriteSpecialistRepository.findByCustomerId(provider.getUserId());
    }
}
