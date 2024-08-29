package fib.br10.dto.gallery.response;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NotNull
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GalleryImageResponse {
    Long id;
    Long imageId;
    String name;
    String path;
    String extension;
}