package fib.br10.mapper;

import fib.br10.dto.specialist.specialistservice.request.CreateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.request.UpdateSpecialistServiceRequest;
import fib.br10.dto.specialist.specialistservice.response.SpecialistServiceResponse;
import fib.br10.entity.specialist.SpecialistService;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialistServiceMapper {

    @Mapping(target = "specialistUserId", source = "specialistUserId")
    @Mapping(target = "imageId", source = "imageId")
    SpecialistService createSpecialistServiceRequestToSpecialistService( CreateSpecialistServiceRequest request, Long specialistUserId, Long imageId);
    SpecialistService updateSpecialistServiceRequestToSpecialistService(@MappingTarget SpecialistService specialistService, UpdateSpecialistServiceRequest request);

    SpecialistServiceResponse specialistServiceToSpecialistServiceResponse(SpecialistService specialistService);
}
    
    
  
 