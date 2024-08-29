package fib.br10.utility;

import fib.br10.core.dto.RequestState;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.DateUtil;
import fib.br10.core.utility.RandomUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class FileUtil {
    RequestContextProvider provider;

    public void createDirectoryIfNotExists(String uploadDir) {
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

    public File convertToFile(MultipartFile file) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            return convFile;
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    public String generateFileName(String fileName) {
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new BaseException();
        }

        String formattedDateTime = DateUtil.getCurrentDateTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));


        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(provider.getUserId())) {
            sb.append(provider.getUserId()).append("_");
        }

        return sb.append(RandomUtil.getUUIDAsStringWithoutDash(provider.getActivityId()))
                .append("_").append(formattedDateTime)
                .toString();
    }

    public String generateFileNameWithExtension(String fileName) {
        String newFileName = generateFileName(fileName);
        String fileExtension = getFileExtension(fileName);
        return newFileName + "." + fileExtension;
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
}
