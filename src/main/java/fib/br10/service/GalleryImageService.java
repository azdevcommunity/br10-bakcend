package fib.br10.service;


import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.gallery.response.GalleryImageResponse;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.entity.GalleryImage;
import fib.br10.exception.fileupload.ImageSaveException;
import fib.br10.mapper.GalleryImageMapper;
import fib.br10.repository.GalleryImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
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
}
