package fib.br10.controller;

import fib.br10.dto.gallery.request.DeleteGalleryRequest;
import fib.br10.dto.gallery.response.GalleryImageResponse;
import fib.br10.service.GalleryImageService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/galleries")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('SPECIALIST')")
public class GalleryController {

    GalleryImageService galleryImageService;

    @PostMapping
    public ResponseEntity<?> createGallery(@RequestPart("images") @Nonnull MultipartFile[] images) {
        return ResponseEntity.ok(galleryImageService.createGalleryImages(images));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/specialist/{specialistId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GalleryImageResponse>> getAll(@PathVariable("specialistId") @Valid Long specialistId) {
        return ResponseEntity.ok(galleryImageService.findAllGalleryImages(specialistId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GalleryImageResponse>> getAll() {
        return ResponseEntity.ok(galleryImageService.findAllGalleryImages());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable("id") @Valid Long id) {
        galleryImageService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@RequestBody @Valid DeleteGalleryRequest request) {
        galleryImageService.delete(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GalleryImageResponse> update(@PathVariable("id") @Valid Long id, @RequestPart("image") @Nonnull MultipartFile image) {
        return ResponseEntity.ok(galleryImageService.update(id, image));
    }
}
