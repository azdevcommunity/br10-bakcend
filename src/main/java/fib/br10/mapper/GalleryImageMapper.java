package fib.br10.mapper;

import fib.br10.dto.gallery.response.GalleryImageResponse;
import fib.br10.dto.image.response.CreateImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GalleryImageMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "imageId", source = "imageResponse.id")
    GalleryImageResponse imageResponsetoGalleryResponse (CreateImageResponse imageResponse,Long id);
}
