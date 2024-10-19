package fib.br10.dto.gallery.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteGalleryRequest {

    @NotNull
    List<Long> galleryIds;
}
