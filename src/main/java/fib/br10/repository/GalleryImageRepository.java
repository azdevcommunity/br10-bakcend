package fib.br10.repository;

import fib.br10.dto.gallery.response.GalleryImageResponse;
import fib.br10.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {


    @Query("""
            select new fib.br10.dto.gallery.response.GalleryImageResponse(gi.id, i.id, i.name, i.path, i.extension)
            from GalleryImage gi
                     left join Image i on gi.imageId = i.id
            where gi.specialistId = :specialistId
            """)
    List<GalleryImageResponse> findAllGalleryImages(Long specialistId);

}
