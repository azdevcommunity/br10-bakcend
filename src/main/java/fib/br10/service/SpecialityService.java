package fib.br10.service;

import fib.br10.core.dto.RequestById;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.dto.speciality.request.CreateSpecialityRequest;
import fib.br10.dto.speciality.request.UpdateSpecialityRequest;
import fib.br10.dto.speciality.response.SpecialityResponse;
import fib.br10.entity.specialist.Speciality;
import fib.br10.exception.speciality.SpecialityNotFoundException;
import fib.br10.mapper.SpecialityMapper;
import fib.br10.repository.SpecialistProfileRepository;
import fib.br10.repository.SpecialityRepository;
import fib.br10.utility.SpecialityUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fib.br10.utility.CacheKeys.SPECIALITIES_KEY;
import static fib.br10.utility.CacheKeys.SPECIALITIES;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpecialityService {

    SpecialistProfileRepository specialistProfileRepository;
    SpecialityRepository specialityRepository;
    SpecialityMapper specialityMapper;

    @CacheEvict(value = SPECIALITIES, key = SPECIALITIES_KEY)
    public Long create(CreateSpecialityRequest request) {
        if (specialityRepository.existsByNameAndStatus(request.getName(), EntityStatus.ACTIVE.getValue())) {
            throw new BaseException("burada error olacaq");
        }

        Speciality speciality = new Speciality();
        speciality = specialityMapper.createSpecialityRequestToEntity(speciality, request);
        specialityRepository.save(speciality);

        return speciality.getId();
    }

    @CacheEvict(value = SPECIALITIES, key = SPECIALITIES_KEY)
    public Long update(UpdateSpecialityRequest request) {
        Speciality speciality = specialityRepository.findByIdAndStatus(request.getId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(SpecialityNotFoundException::new);

        if (specialityRepository.existsByNameAndStatusAndIdNot(request.getName(),
                EntityStatus.ACTIVE.getValue(), request.getId())) {
            throw new BaseException("burada error olacaq");
        }

        speciality = specialityMapper.updateSpecialityRequestToEntity(speciality, request);
        specialityRepository.save(speciality);

        return speciality.getId();
    }

    @CacheEvict(value = SPECIALITIES, key = SPECIALITIES_KEY)
    @Transactional
    public Long delete(RequestById request) {
        if (specialistProfileRepository.existsBySpecialityId(request.getId())) {
            throw new BaseException("burada exception olacaq");
        }

        Speciality speciality = specialityRepository.findByIdAndStatus(request.getId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(SpecialityNotFoundException::new);

        speciality.setStatus(EntityStatus.DELETED.getValue());
        specialityRepository.save(speciality);

        return speciality.getId();
    }

    @Cacheable(value = SPECIALITIES, key = SPECIALITIES_KEY)
    public List<SpecialityResponse> findAll() {
        return specialityRepository
                .findByStatusOrderByCreatedDateDesc(EntityStatus.ACTIVE.getValue())
                .stream()
                .map(specialityMapper::toSpecialityResponse)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Speciality findById(Long id) {
        return specialityRepository.findByIdAndStatus(id, EntityStatus.ACTIVE.getValue())
                .orElseThrow(SpecialityNotFoundException::new);
    }

    public void checkSpecialityExists(Long specialityId) {
        boolean exists = specialityRepository.existsByIdAndStatus(specialityId, EntityStatus.ACTIVE.getValue());
        if (!exists) {
            throw new SpecialityNotFoundException();
        }
    }

    public String findSpecialistyName(Long id) {
        return SpecialityUtil.getSpecialityName(findById(id));
    }
}
