package fib.br10.service;

import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.dto.specialist.specialistprofile.request.CreateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.request.UpdateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse;
import fib.br10.entity.Image;
import fib.br10.entity.specialist.SpecialistProfile;
import fib.br10.entity.specialist.Speciality;
import fib.br10.exception.specialist.specialistprofile.SpecialistProfileAlreadyExists;
import fib.br10.exception.specialist.specialistprofile.SpecialistProfileNotFoundException;
import fib.br10.mapper.SpecialistProfileMapper;
import fib.br10.repository.SpecialistProfileRepository;
import fib.br10.utility.SpecialityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static fib.br10.utility.CacheKeys.SPECIALIST_PROFILE;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class SpecialistProfileService {
    UserService userService;
    SpecialistProfileRepository specialistProfileRepository;
    SpecialistProfileMapper specialistProfileMapper;
    SpecialityService specialityService;
    ImageService imageService;

    @Cacheable(value = SPECIALIST_PROFILE, key = "#id")
    public SpecialistProfileReadResponse read(Long id) {
        //TODO: refactor this select image with join
        SpecialistProfile specialistProfile = findBySpecialistUserId(id);
        SpecialistProfileReadResponse response = specialistProfileMapper.specialistProfileToSpecialistProfileResponse(specialistProfile);

        if (Objects.nonNull(specialistProfile.getImageId())) {
            Image image = imageService.findById(specialistProfile.getImageId());
            if (Objects.nonNull(image))
                response.setProfilePicture(image.getPath());
        }
        Speciality speciality = specialityService.findById(response.getSpecialityId());
        response.setSpeciality(SpecialityUtil.getSpecialityName(speciality));
        return response;
    }

    @CacheEvict(value = SPECIALIST_PROFILE, key = "#userId")
    public Long update(UpdateSpecialistProfileRequest request, Long userId) {
        SpecialistProfile specialistProfile = findBySpecialistUserId(userId);
        specialityService.checkSpecialityExists(request.getSpecialityId());
        specialistProfile = specialistProfileMapper.updateSpecialistProfileRequestToSpecialistProfile(specialistProfile, request);
        specialistProfileRepository.save(specialistProfile);
        return specialistProfile.getId();
    }

    @Transactional
    public Long create(CreateSpecialistProfileRequest request) {
        userService.validateUserExists(request.getSpecialistUserId());

        if (specialistProfileRepository.existsBySpecialistUserId(request.getSpecialistUserId())) {
            throw new SpecialistProfileAlreadyExists();
        }
        SpecialistProfile specialistProfile = new SpecialistProfile();
        specialistProfile = specialistProfileMapper.specialistProfileRequestToSpecialistProfile(specialistProfile, request);
        specialistProfileRepository.save(specialistProfile);
        return specialistProfile.getId();
    }

    public SpecialistProfile findBySpecialistUserId(Long userId) {
        return specialistProfileRepository.findBySpecialistUserId(userId)
                .orElseThrow(SpecialistProfileNotFoundException::new);
    }

    @CacheEvict(value = SPECIALIST_PROFILE, key = "#userId")
    public Long update(MultipartFile file, Long userId) {
        SpecialistProfile specialistProfile = findBySpecialistUserId(userId);

        if (Objects.nonNull(specialistProfile.getImageId())) {
            Image image = imageService.findById(specialistProfile.getImageId());
            if (Objects.nonNull(image))
                imageService.delete(image.getId());
        }
        CreateImageResponse response = imageService.create(file);
        specialistProfile.setImageId(response.getId());
        specialistProfileRepository.save(specialistProfile);
        return specialistProfile.getId();
    }
}