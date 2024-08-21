package fib.br10.service;

import fib.br10.core.entity.EntityStatus;
import fib.br10.core.enums.DayOfWeek;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.specialist.specialistavailability.request.CreateSpecialistAvailabilityRequest;
import fib.br10.dto.specialist.specialistavailability.response.SpecialistAvailabilityReadResponse;
import fib.br10.entity.specialist.SpecialistAvailability;
import fib.br10.mapper.SpecialistAvailabilityMapper;
import fib.br10.repository.SpecialistAvailabilityRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpecialistAvailabilityService {

    SpecialistAvailabilityRepository specialistAvailabilityRepository;
    SpecialistAvailabilityMapper specialistAvailabilityMapper;
    RequestContextProvider provider;

    public List<SpecialistAvailabilityReadResponse> read() {
        return specialistAvailabilityRepository
                .findAllBySpecialistUserIdAndStatus(provider.getUserId(), EntityStatus.ACTIVE.getValue())
                .stream()
                .map(specialistAvailabilityMapper::toSpecialistAvailabilityReadResponse)
                .toList();
    }

    public Long crate(CreateSpecialistAvailabilityRequest request) {
        Long userId = provider.getUserId();

        validateSpecialistAvailability(request);

        if (specialistAvailabilityRepository.existsBySpecialistUserIdAndDayOfWeek(userId, request.getDayOfWeek().getValue())) {
            throw new BaseException("Specialist availability already exists for this day of week.");
        }

        SpecialistAvailability specialistAvailability = SpecialistAvailability.builder()
                .specialistUserId(userId)
                .dayOfWeek(request.getDayOfWeek().getValue())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isWeekend(request.getIsWeekend())
                .build();

        specialistAvailabilityRepository.save(specialistAvailability);

        return specialistAvailability.getId();
    }

    public void addWeekendAvailability(Long specialistUserId) {
        SpecialistAvailability specialistAvailability = SpecialistAvailability.builder()
                .specialistUserId(specialistUserId)
                .dayOfWeek(DayOfWeek.SATURDAY.getValue())
                .isWeekend(true)
                .build();

        SpecialistAvailability specialistAvailability2 = SpecialistAvailability.builder()
                .specialistUserId(specialistUserId)
                .dayOfWeek(DayOfWeek.SUNDAY.getValue())
                .isWeekend(true)
                .build();

        specialistAvailabilityRepository.saveAll(List.of(specialistAvailability, specialistAvailability2));
    }

    private void validateSpecialistAvailability(CreateSpecialistAvailabilityRequest request) {
        boolean isWeekendNull = Objects.isNull(request.getIsWeekend());
        boolean startTimeNull = Objects.isNull(request.getStartTime());
        boolean endTimeNull = Objects.isNull(request.getEndTime());

        if (isWeekendNull && (!startTimeNull || !endTimeNull)) {
            throw new BaseException("Both startTime and endTime must be null if isWeekend is null.");
        }

        if (!isWeekendNull && (startTimeNull || endTimeNull)) {
            throw new BaseException("Neither startTime nor endTime can be null if isWeekend is not null.");
        }
    }
}
