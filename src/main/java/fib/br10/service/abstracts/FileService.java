package fib.br10.service.abstracts;

import fib.br10.core.dto.RequestState;
import fib.br10.dto.file.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileService {
   FileUploadResponse uploadFile(MultipartFile file);
   void deleteFile(List<String> fileName);
}
