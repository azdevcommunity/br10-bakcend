package fib.br10.service;


import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.gallery.response.DeleteGalleriesRequest;
import fib.br10.dto.gallery.response.GalleryImageResponse;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.entity.GalleryImage;
import fib.br10.exception.fileupload.ImageSaveException;
import fib.br10.exception.galleryimage.GalleryImageNotFoundException;
import fib.br10.mapper.GalleryImageMapper;
import fib.br10.repository.GalleryImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class GalleryImageService {

    GalleryImageRepository galleryImageRepository;
    RequestContextProvider provider;
    GalleryImageMapper galleryImageMapper;
    ImageService imageService;
    UserService userService;

    public List<GalleryImageResponse> createGalleryImages(MultipartFile[] images) {
        List<GalleryImageResponse> responses = new ArrayList<>();
        for (MultipartFile image : images) {
            CreateImageResponse imageResponse = null;
            try {
                imageResponse = imageService.create(image);
                GalleryImage galleryImage = new GalleryImage(provider.getUserId(), imageResponse.getId());
                galleryImage = galleryImageRepository.saveAndFlush(galleryImage);
                responses.add(galleryImageMapper.imageResponsetoGalleryResponse(imageResponse, galleryImage.getId()));
            } catch (Exception e) {
                if (!(e instanceof ImageSaveException) && Objects.nonNull(imageResponse)) {
                    imageService.delete(imageResponse.getId());
                }
            }
        }
        return responses;
    }

    public List<GalleryImageResponse> findAllGalleryImages(Long specialistId) {
        userService.existsByIdAndUserRoleSpecialist(specialistId);
        return galleryImageRepository.findAllGalleryImages(specialistId);
    }

    public List<GalleryImageResponse> findAllGalleryImages() {
        return galleryImageRepository.findAllGalleryImages(provider.getUserId());
    }

    @Transactional
    public void delete(Long id) {
        GalleryImage galleryImage = findById(id);
        galleryImageRepository.delete(galleryImage);
        imageService.delete(galleryImage.getImageId());
    }

    @Transactional
    public void delete(DeleteGalleriesRequest request) {
        request.getGalleryIds().forEach(this::delete);
    }

    public GalleryImageResponse update(Long id, MultipartFile image) {
        if (Objects.isNull(image)) {
            throw new BaseException("Image is required");
        }
        GalleryImage galleryImage = findById(id);
        CreateImageResponse imageResponse = null;
        try {
            imageService.delete(galleryImage.getImageId());
            imageResponse = imageService.create(image);
            galleryImage.setImageId(imageResponse.getId());
            galleryImage = galleryImageRepository.saveAndFlush(galleryImage);
            return galleryImageMapper.imageResponsetoGalleryResponse(imageResponse, galleryImage.getId());
        } catch (Exception e) {
            if (!(e instanceof ImageSaveException) && Objects.nonNull(imageResponse)) {
                imageService.delete(imageResponse.getId());
            }
            throw new BaseException(e.getMessage());
        }
    }

    public GalleryImage findById(Long id) {
        return galleryImageRepository.findById(id)
                .orElseThrow(GalleryImageNotFoundException::new);
    }
}
