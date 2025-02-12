package fib.br10.mapper;

import fib.br10.dto.specialist.favoritespecialist.request.FavoriteSpecialistCreateRequest;
import fib.br10.dto.specialist.favoritespecialist.response.FavoriteSpecialistResponse;
import fib.br10.entity.specialist.FavoriteSpecialist;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoriteSpecialistMapper {


    FavoriteSpecialist toEntity(FavoriteSpecialistCreateRequest request);

    FavoriteSpecialistResponse toResponse(FavoriteSpecialist entity);

    List<FavoriteSpecialistResponse> toResponseList(List<FavoriteSpecialist> entities);
}
