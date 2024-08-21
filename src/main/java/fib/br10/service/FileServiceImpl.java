package fib.br10.service;

import fib.br10.core.dto.RequestState;
import fib.br10.core.exception.BaseException;
import fib.br10.core.utility.DateUtil;

import fib.br10.core.utility.RandomUtil;
import fib.br10.dto.file.response.FileUploadResponse;
import fib.br10.service.abstracts.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.upload-dir}")
    String uploadDir;


    @Override
    public CompletableFuture<FileUploadResponse> uploadFile(MultipartFile file, RequestState requestState) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                createDirectoryIfNotExists();
                String fileName = generateFileName(file.getOriginalFilename(), requestState);
                Path uploadPath = Paths.get(uploadDir);
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                return FileUploadResponse.builder()
                        .path(filePath.toAbsolutePath().toString())
                        .name(fileName)
                        .extension(getFileExtension(fileName))
                        .build();
            } catch (IOException e) {
                log.error("Error while uploading file", e);
                throw new BaseException();
            }
        });
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            Path filePath = Paths.get(fileUrl);
            if (!Files.exists(filePath)) {
                log.info("File not found on path {}", filePath);
                return;
            }
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error while deleting file", e);
            throw new BaseException();
        }
    }


    public String generateFileName(String fileName, RequestState requestState) {
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new BaseException();
        }

        String formattedDateTime = DateUtil.getCurrentDateTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        String fileExtension = getFileExtension(fileName);

        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(requestState.userId())) {
            sb.append(requestState.userId()).append("_");
        }

        return sb.append(RandomUtil.getUUIDAsStringWithoutDash(requestState.activityId()))
                .append("_").append(formattedDateTime)
                .append(".").append(fileExtension)
                .toString();
    }

    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }

        return "";
    }

    public void createDirectoryIfNotExists() {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Error while creating directory", e);
                throw new BaseException();
            }
        }
    }
}
