package fib.br10.service;


import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.dto.specialist.specialistprofile.request.CreateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.request.ReadByPhoneNumbersRequest;
import fib.br10.dto.specialist.specialistprofile.request.UpdateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse;
import fib.br10.entity.Image;
import fib.br10.entity.specialist.SpecialistProfile;
import fib.br10.entity.user.User;
import fib.br10.exception.specialist.specialistprofile.SpecialistProfileAlreadyExists;
import fib.br10.exception.specialist.specialistprofile.SpecialistProfileNotFoundException;
import fib.br10.mapper.SpecialistProfileMapper;
import fib.br10.repository.SpecialistProfileRepository;
import fib.br10.service.abstracts.ImageService;
import fib.br10.service.abstracts.SpecialityManager;
import fib.br10.enumeration.LangEnum;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
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
    SpecialityManager specialityService;
    ImageService imageService;
    RequestContextProvider provider;

    @Cacheable(value = SPECIALIST_PROFILE, key = "#id")
    public SpecialistProfileReadResponse read(Long id) {
        return specialistProfileRepository.findByUserId(
                id,
                EntityStatus.ACTIVE.getValue(),
                LangEnum.AZ.getCode()
        );
    }

    @CacheEvict(value = SPECIALIST_PROFILE, key = "#userId")
    public SpecialistProfileReadResponse update(UpdateSpecialistProfileRequest request, Long userId) {
        SpecialistProfile specialistProfile = findBySpecialistUserId(userId);
        specialityService.checkSpecialityExists(request.getSpecialityId());
        specialistProfile = specialistProfileMapper.updateSpecialistProfileRequestToSpecialistProfile(specialistProfile, request);
        specialistProfileRepository.save(specialistProfile);

        User user = userService.findById(userId);
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        userService.save(user);

        SpecialistProfileReadResponse response = specialistProfileMapper.specialistProfileToSpecialistProfileResponse(specialistProfile);
        response.setSpeciality(specialityService.findSpecialistyName(response.getSpecialityId()));
        Image image = imageService.findById(specialistProfile.getImageId());
        if (Objects.nonNull(image)) {
            response.setProfilePicture(image.getPath());
        }
        response.setName(user.getName());
        response.setSurname(user.getSurname());
        response.setPhoneNumber(user.getPhoneNumber());
        return response;
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
    public SpecialistProfileReadResponse update(MultipartFile file, Long userId) {
        SpecialistProfile specialistProfile = findBySpecialistUserId(userId);

        if (Objects.nonNull(specialistProfile.getImageId())) {
            Image image = imageService.findById(specialistProfile.getImageId());
            if (Objects.nonNull(image))
                imageService.delete(image.getId());
        }
        CreateImageResponse imageResponse = imageService.create(file);
        specialistProfile.setImageId(imageResponse.getId());
        specialistProfileRepository.save(specialistProfile);

        SpecialistProfileReadResponse response = specialistProfileMapper.specialistProfileToSpecialistProfileResponse(specialistProfile);
        response.setProfilePicture(imageResponse.getPath());
        response.setSpeciality(specialityService.findSpecialistyName(response.getSpecialityId()));

        User user = userService.findById(userId);
        response.setName(user.getName());
        response.setSurname(user.getSurname());
        response.setPhoneNumber(user.getPhoneNumber());
        return response;
    }

    public List<SpecialistProfileReadResponse> read(ReadByPhoneNumbersRequest request) {
        if (ObjectUtils.isEmpty(request.getPhoneNumbers())) {
            return Collections.emptyList();
        }

        return specialistProfileRepository.findAllByPhoneNumbers(
                request.getPhoneNumbers(),
                EntityStatus.ACTIVE.getValue(),
                LangEnum.fromValue(provider.getLang()).getCode()
        );
    }

    public SpecialistProfileReadResponse read(String phoneNumber) {
        if (Objects.isNull(phoneNumber)) {
            throw new BaseException("Nomre bos ola bilmez");
        }

        return specialistProfileRepository.findByPhoneNumber(
                phoneNumber,
                EntityStatus.ACTIVE.getValue(),
                LangEnum.fromValue(provider.getLang()).getCode()
        );
    }


    @Cacheable(value = SPECIALIST_PROFILE, key = "#search")
    public List<SpecialistProfileReadResponse> readBySearch(String search) {
        if (Objects.isNull(search)) {
            throw new BaseException("Search bos ola bilmez");
        }

        return specialistProfileRepository.findBySearch(
                search,
                EntityStatus.ACTIVE.getValue(),
                LangEnum.fromValue(provider.getLang()).getCode()
        );
    }
}