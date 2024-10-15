package fib.br10.dto.gallery.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteGalleriesRequest {

    @NotNull
    List<Long> galleryIds;
}
