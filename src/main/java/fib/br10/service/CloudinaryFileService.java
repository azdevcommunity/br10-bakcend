package fib.br10.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import fib.br10.core.dto.RequestState;
import fib.br10.core.exception.BaseException;
import fib.br10.dto.file.response.FileUploadResponse;
import fib.br10.service.abstracts.FileService;
import fib.br10.utility.CloudinaryKeys;
import fib.br10.utility.FileUtil;
import io.jsonwebtoken.lang.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static fib.br10.utility.CloudinaryKeys.PUBLIC_ID;
import static fib.br10.utility.CloudinaryKeys.RESOURCE_TYPE;
import static fib.br10.utility.CloudinaryKeys.RESOURCE_TYPE_IMAGE;
import static fib.br10.utility.CloudinaryKeys.TYPE;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CloudinaryFileService implements FileService {

    Cloudinary cloudinary;
    FileUtil fileUtil;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) {
        try {
            File uploadFile = fileUtil.convertToFile(file);
            String fileName = fileUtil.generateFileName(file.getOriginalFilename());

            Map optios = com.cloudinary.utils.ObjectUtils.asMap(
                    PUBLIC_ID, fileName,
                    RESOURCE_TYPE, RESOURCE_TYPE_IMAGE
            );

            Map<String, Object> response = cloudinary.uploader().upload(uploadFile, optios);

            String path = (String) response.getOrDefault(CloudinaryKeys.SECURE_URL, "");
            String extension = (String) response.getOrDefault(CloudinaryKeys.FORMAT, "");
            String name = (String) response.getOrDefault(CloudinaryKeys.DISPLAY_NAME, "");

            if (ObjectUtils.isEmpty(path) || ObjectUtils.isEmpty(extension) || ObjectUtils.isEmpty(name)) {
                throw new BaseException();
            }

            return FileUploadResponse.builder()
                    .path(path)
                    .extension(extension)
                    .name(name)
                    .build();
        } catch (IOException e) {
            log.error("Error while uploading file", e);
            throw new BaseException();
        }
    }

    @Override
    public void deleteFile(List<String> fileName) {
        try {
            if(ObjectUtils.isEmpty(fileName)){
                return;
            }
            ApiResponse apiResponse = cloudinary.api().deleteResources(fileName,
                    com.cloudinary.utils.ObjectUtils.asMap(TYPE, CloudinaryKeys.UPLOAD, RESOURCE_TYPE, RESOURCE_TYPE_IMAGE));
            log.info(apiResponse);
        } catch (Exception exception) {
            throw new BaseException(exception.getMessage());
        }
    }
}
