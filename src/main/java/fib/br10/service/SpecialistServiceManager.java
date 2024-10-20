package fib.br10.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fib.br10.core.dto.RequestById;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.dto.specialist.specialistservice.request.CreateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.request.UpdateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.response.SpecialistServiceResponse;
import fib.br10.entity.Image;
import fib.br10.entity.QImage;
import fib.br10.entity.specialist.QSpecialistService;
import fib.br10.entity.specialist.SpecialistService;
import fib.br10.exception.specialist.specialistservice.SpecialistServiceAlreadyExistsException;
import fib.br10.exception.specialist.specialistservice.SpecialistServiceNotFoundException;
import fib.br10.mapper.SpecialistServiceMapper;
import fib.br10.repository.ReservationRepository;
import fib.br10.repository.SpecialistServiceRepository;
import fib.br10.service.abstracts.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    ReservationRepository reservationRepository;

    @CacheEvict(value = SPECIALIST_SERVICES, key = "#userId")
    @Transactional
    public SpecialistServiceResponse create(CreateSpecialistServiceRequest request, Long userId) {
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

            SpecialistServiceResponse response = specialistServicesMapper.specialistServiceToSpecialistServiceResponse(specialistService);
            if (Objects.nonNull(imageResponse)) {
                response.setImage(imageResponse.getPath());
            }
            return response;

        } catch (Exception e) {
            if (Objects.nonNull(imageResponse)) {
                imageService.delete(imageResponse.getName());
            }
            throw e;
        }
    }

    @CacheEvict(value = SPECIALIST_SERVICES, key = "#userId")
    @Transactional
    public SpecialistServiceResponse update(UpdateSpecialistServiceRequest request, Long userId) {
        userService.existsByIdAndUserRoleSpecialist(userId);

        if (specialistServicesRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new SpecialistServiceAlreadyExistsException();
        }

        SpecialistService specialistService = specialistServicesRepository.findById(request.getId())
                .orElseThrow(SpecialistServiceNotFoundException::new);

        specialistService = specialistServicesMapper.updateSpecialistServiceRequestToSpecialistService(specialistService, request);

        specialistServicesRepository.save(specialistService);

        SpecialistServiceResponse response = specialistServicesMapper.specialistServiceToSpecialistServiceResponse(specialistService);

        Image image = imageService.findById(specialistService.getImageId());

        if(Objects.nonNull(image)){
            response.setImage(image.getPath());
        }

        return response;
    }

    @CacheEvict(value = SPECIALIST_SERVICES, key = "#userId")
    @Transactional
    public Long delete(RequestById request, Long userId) {
        SpecialistService specialistService = specialistServicesRepository
                .findByIdAndSpecialistUserId(request.getId(), userId)
                .orElseThrow(SpecialistServiceNotFoundException::new);
//
//        if (reservationRepository.existsBySpecialistServiceIdAndStatusNot(request.getId(), ReservationStatus.CANCELLED.getValue())) {
//            throw new ServiceAlreadyUsedOnAnyReservationException();
//        }

        specialistServicesRepository.delete(specialistService);

        if (Objects.nonNull(specialistService.getImageId())) {
            imageService.delete(specialistService.getImageId());
        }

        return specialistService.getId();
    }

    @CacheEvict(value = SPECIALIST_SERVICES, key = "#userId")
    @Transactional
    public SpecialistServiceResponse update(MultipartFile image, Long id, Long userId) {
        SpecialistService specialistService = findById(id);
        Long oldId  = specialistService.getImageId();

        CreateImageResponse createImageResponse = imageService.create(image);

        specialistService.setImageId(createImageResponse.getId());
        specialistServicesRepository.save(specialistService);

        if(Objects.nonNull(oldId)){
            imageService.delete(oldId);
        }
        SpecialistServiceResponse response = specialistServicesMapper.specialistServiceToSpecialistServiceResponse(specialistService);
        response.setImage(createImageResponse.getPath());
        return response;
    }

    @Cacheable(value = SPECIALIST_SERVICES, key = "#specialistId")
    public List<SpecialistServiceResponse> findAllSpecialistServices(Long specialistId) {
        QSpecialistService table = QSpecialistService.specialistService;
        QImage image = QImage.image;

        List<SpecialistServiceResponse> specialistServices = jpaQuery
                .select(Projections.constructor(SpecialistServiceResponse.class,
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
                .where(table.specialistUserId.eq(specialistId))
                .orderBy(table.id.desc())
                .fetch();

        return specialistServices;
    }

    public SpecialistServiceResponse findServiceById(Long id) {
        SpecialistServiceResponse specialistService = specialistServicesRepository.findSpecialist(id)
                .orElseThrow(SpecialistServiceNotFoundException::new);

        return specialistService;
    }

    public SpecialistService findById(Long id) {
        return specialistServicesRepository.findById(id)
                .orElseThrow(SpecialistServiceNotFoundException::new);
    }

    public List<SpecialistService> findAllByIds(List<Long> id) {
        return specialistServicesRepository.findAllByIdIn(id);
    }



}