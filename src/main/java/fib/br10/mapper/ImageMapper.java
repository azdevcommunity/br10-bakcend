package fib.br10.mapper;

import fib.br10.dto.file.response.FileUploadResponse;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

    Image fileUploadResonseToImage(FileUploadResponse response);

    CreateImageResponse imageToCreateImageResponse(Image image);
}
