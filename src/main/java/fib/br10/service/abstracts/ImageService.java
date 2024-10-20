package fib.br10.service.abstracts;

import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.entity.Image;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {

    CreateImageResponse create(MultipartFile file);

    void delete(Long id);

    void delete(String name);

    Image findById(Long id);
}
