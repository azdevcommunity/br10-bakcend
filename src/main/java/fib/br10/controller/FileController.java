package fib.br10.controller;


import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.file.response.FileUploadResponse;

import fib.br10.service.abstracts.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileService fileService;
    private final RequestContextProvider provider;

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<FileUploadResponse>> uploadImage(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file,provider.getRequestState()).thenApplyAsync(ResponseEntity::ok);
    }
}
