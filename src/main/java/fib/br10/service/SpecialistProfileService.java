package fib.br10.service;

import fib.br10.core.dto.RequestById;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.file.response.FileUploadResponse;
import fib.br10.dto.specialist.specialistprofile.request.CreateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.request.UpdateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse;
import fib.br10.entity.specialist.SpecialistProfile;
import fib.br10.entity.specialist.Speciality;
import fib.br10.exception.specialist.specialistprofile.SpecialistProfileAlreadyExists;
import fib.br10.exception.specialist.specialistprofile.SpecialistProfileNotFoundException;
import fib.br10.mapper.SpecialistProfileMapper;
import fib.br10.repository.SpecialistProfileRepository;
import fib.br10.service.abstracts.FileService;
import fib.br10.utility.SpecialityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static fib.br10.utility.CacheKeys.SPECIALIST_PROFILE;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class SpecialistProfileService{
    UserService userService;
    SpecialistProfileRepository specialistProfileRepository;
    SpecialistProfileMapper specialistProfileMapper;
    FileService fileService;
    SpecialityService specialityService;
    RequestContextProvider provider;

    @Cacheable(value = SPECIALIST_PROFILE, key = "#request.id")
    public SpecialistProfileReadResponse read(RequestById request) {
        SpecialistProfile specialistProfile = findBySpecialistUserId(request.getId());
        SpecialistProfileReadResponse response = specialistProfileMapper.specialistProfileToSpecialistProfileResponse(specialistProfile);

        Speciality speciality = specialityService.findById(response.getSpecialityId());
        response.setSpeciality(SpecialityUtil.getSpecialityName(speciality));

        return response;
    }

    @CacheEvict(value = SPECIALIST_PROFILE, key = "#userId")
    public Long update(UpdateSpecialistProfileRequest request, MultipartFile file, Long userId) {
        request.setProfilePicture(file);

        SpecialistProfile specialistProfile = findBySpecialistUserId(userId);

        if (Objects.isNull(file)) {
            fileService.deleteFile(specialistProfile.getProfilePicture());
            specialistProfile.setProfilePicture(null);
        } else {
            FileUploadResponse response = fileService.uploadFile(request.getProfilePicture(),provider.getRequestState()).join();
            specialistProfile.setProfilePicture(response.getPath());
        }

        specialistProfile = specialistProfileMapper.updateSpecialistProfileRequestToSpecialistProfile(specialistProfile, request);

        specialistProfileRepository.save(specialistProfile);

        return specialistProfile.getId();
    }

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
}