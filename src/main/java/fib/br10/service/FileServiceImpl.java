package fib.br10.service;

import fib.br10.core.exception.BaseException;
import fib.br10.dto.file.response.FileUploadResponse;
import fib.br10.utility.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
public class FileServiceImpl {

    @Value("${file.upload-dir}")
    String uploadDir;
    FileUtil fileUtil;

    public CompletableFuture<FileUploadResponse> uploadFile(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                fileUtil.createDirectoryIfNotExists(uploadDir);
                String fileName = fileUtil.generateFileNameWithExtension(file.getOriginalFilename());
                Path uploadPath = Paths.get(uploadDir);
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                return FileUploadResponse.builder()
                        .path(filePath.toAbsolutePath().toString())
                        .name(fileName)
                        .extension(fileUtil.getFileExtension(fileName))
                        .build();
            } catch (IOException e) {
                log.error("Error while uploading file", e);
                throw new BaseException();
            }
        });
    }

    public void deleteFile(String fileName) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName);

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
}
