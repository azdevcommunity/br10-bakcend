package fib.br10.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fib.br10.core.dto.RequestById;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.dto.specialist.specialistservice.request.CreateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.request.GetSpecialistServicesRequest;
import fib.br10.dto.specialist.specialistservice.request.UpdateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.response.ReadSpecialistServiceResponse;
import fib.br10.entity.QImage;
import fib.br10.entity.specialist.QSpecialistService;
import fib.br10.entity.specialist.SpecialistService;
import fib.br10.exception.specialist.specialistservice.ServiceAlreadyUsedOnAnyReservationException;
import fib.br10.exception.specialist.specialistservice.SpecialistServiceAlreadyExistsException;
import fib.br10.exception.specialist.specialistservice.SpecialistServiceNotFoundException;
import fib.br10.mapper.SpecialistServiceMapper;
import fib.br10.repository.SpecialistServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fib.br10.utility.CacheKeys.SPECIALIST_SERVICES;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class SpecialistServiceManager {

    SpecialistServiceRepository specialistServicesRepository;
    SpecialistServiceMapper specialistServicesMapper;
    UserService userService;
    JPAQueryFactory jpaQuery;
    ImageService imageService;
    RequestContextProvider provider;
    ReservationService reservationService;

    @CacheEvict(value = SPECIALIST_SERVICES, key = "#userId")
    @Transactional
    public Long create(CreateSpecialistServiceRequest request, Long userId) {
        userService.existsByIdAndUserRoleSpecialist(userId);

        if (specialistServicesRepository.existsByName(request.getName())) {
            throw new SpecialistServiceAlreadyExistsException();
        }

        CreateImageResponse imageResponse = null;
        Long imageId = null;

        try {
            if (Objects.nonNull(request.getImage())) {
                imageResponse = imageService.create(request.getImage());
                imageId = imageResponse.getId();
            }

            SpecialistService specialistService = specialistServicesMapper.createSpecialistServiceRequestToSpecialistService(request, userId, imageId);
            specialistServicesRepository.save(specialistService);
            return specialistService.getId();
        } catch (Exception e) {
            if (Objects.nonNull(imageResponse)) {
                imageService.delete(imageResponse.getPath());
            }
            throw e;
        }
    }

    @CacheEvict(value = SPECIALIST_SERVICES, key = "#userId")
    @Transactional
    public Long update(UpdateSpecialistServiceRequest request, Long userId) {
        userService.existsByIdAndUserRoleSpecialist(userId);

        if (specialistServicesRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new SpecialistServiceAlreadyExistsException();
        }

        SpecialistService specialistService = specialistServicesRepository.findById(request.getId())
                .orElseThrow(SpecialistServiceNotFoundException::new);

        if (Objects.isNull(request.getImage())) {
            imageService.delete(specialistService.getImageId());
            specialistService.setImageId(null);
        } else {
            CreateImageResponse response = imageService.create(request.getImage());
            specialistService.setImageId(response.getId());
        }

        specialistService = specialistServicesMapper.updateSpecialistServiceRequestToSpecialistService(specialistService, request);

        specialistServicesRepository.save(specialistService);
        return specialistService.getId();
    }

    @CacheEvict(value = SPECIALIST_SERVICES, key = "#userId")
    @Transactional
    public Long delete(RequestById request, Long userId) {
        SpecialistService specialistService = specialistServicesRepository
                .findByIdAndSpecialistUserId(request.getId(), userId)
                .orElseThrow(SpecialistServiceNotFoundException::new);

        if(reservationService.existsActiveReservationByServiceId(request.getId())){
            throw new ServiceAlreadyUsedOnAnyReservationException();
        }

        specialistServicesRepository.delete(specialistService);

        if (Objects.nonNull(specialistService.getImageId())) {
            imageService.delete(specialistService.getImageId());
        }

        return specialistService.getId();
    }

    @Cacheable(value = SPECIALIST_SERVICES, key = "#request.id")
    public List<ReadSpecialistServiceResponse> findAllSpecialistServices(GetSpecialistServicesRequest request) {
        QSpecialistService table = QSpecialistService.specialistService;
        QImage image = QImage.image;

        if(Objects.isNull(request.getId())){
            request.setId(provider.getUserId());
        }

        List<ReadSpecialistServiceResponse> specialistServices = jpaQuery
                .select(Projections.constructor(ReadSpecialistServiceResponse.class,
                        table.id,
                        table.specialistUserId,
                        table.duration,
                        table.name,
                        table.price,
                        table.description,
                        image.path))
                .from(table)
                .leftJoin(image)
                .on(table.imageId.eq(image.id))
                .where(table.specialistUserId.eq(request.getId()))
                .fetch();

        return new ArrayList<>(specialistServices);
    }

    public SpecialistService findById(Long id) {
        return specialistServicesRepository.findById(id)
                .orElseThrow(SpecialistServiceNotFoundException::new);
    }
}