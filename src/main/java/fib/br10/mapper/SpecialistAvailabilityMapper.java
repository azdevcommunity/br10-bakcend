package fib.br10.mapper;

import fib.br10.dto.specialist.specialistavailability.response.SpecialistAvailabilityReadResponse;
import fib.br10.entity.specialist.SpecialistAvailability;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialistAvailabilityMapper {

    SpecialistAvailabilityReadResponse toSpecialistAvailabilityReadResponse(SpecialistAvailability specialistAvailability);
}
    
    
  
 