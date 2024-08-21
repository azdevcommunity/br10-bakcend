package fib.br10.mapper;

import fib.br10.dto.speciality.request.CreateSpecialityRequest;
import fib.br10.dto.speciality.request.UpdateSpecialityRequest;
import fib.br10.dto.speciality.response.SpecialityResponse;
import fib.br10.entity.specialist.Speciality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialityMapper {

    Speciality createSpecialityRequestToEntity(@MappingTarget Speciality speciality, CreateSpecialityRequest request);
    Speciality updateSpecialityRequestToEntity(@MappingTarget Speciality speciality, UpdateSpecialityRequest request);

    @Mapping(target = "name", expression = "java(fib.br10.utility.SpecialityUtil.getSpecialityName(speciality))")
    SpecialityResponse toSpecialityResponse(Speciality speciality);


}
