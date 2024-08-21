package fib.br10.service;

import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.file.response.FileUploadResponse;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.entity.Image;
import fib.br10.mapper.ImageMapper;
import fib.br10.repository.ImageRepository;
import fib.br10.service.abstracts.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class ImageService {

    ImageRepository imageRepository;
    FileService fileService;
    ImageMapper imageMapper;
    RequestContextProvider provider;

    @Transactional
    public CreateImageResponse create(MultipartFile file) {
        FileUploadResponse response = null;
        try {
            response = fileService.uploadFile(file, provider.getRequestState()).join();

            Image image = imageMapper.fileUploadResonseToImage(response);

            imageRepository.saveAndFlush(image);
            return imageMapper.imageToCreateImageResponse(image);
        } catch (Exception e) {
            log.error(e);
            if (Objects.nonNull(response)) {
                fileService.deleteFile(response.getPath());
            }
            throw new BaseException("error occurred");
        }
    }

    @Transactional
    public void delete(Long id){
        Image image = imageRepository.findById(id).orElseThrow(BaseException::new);

        imageRepository.delete(image);
        fileService.deleteFile(image.getPath());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void delete(String path){
        fileService.deleteFile(path);
    }
}
