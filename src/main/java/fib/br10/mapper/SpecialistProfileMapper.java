package fib.br10.mapper;

import fib.br10.dto.specialist.specialistprofile.request.CreateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.request.UpdateSpecialistProfileRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse;
import fib.br10.entity.specialist.SpecialistProfile;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialistProfileMapper {

    SpecialistProfile specialistProfileRequestToSpecialistProfile(@MappingTarget SpecialistProfile specialistProfile, CreateSpecialistProfileRequest request);

    @Mapping(target = "profilePicture", ignore = true)
    SpecialistProfile updateSpecialistProfileRequestToSpecialistProfile(@MappingTarget SpecialistProfile specialistProfile, UpdateSpecialistProfileRequest request);

    SpecialistProfileReadResponse specialistProfileToSpecialistProfileResponse(SpecialistProfile specialistProfile);
}
    
    
  
 