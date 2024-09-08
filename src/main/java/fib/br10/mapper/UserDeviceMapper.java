package fib.br10.mapper;


import fib.br10.dto.userdevice.request.UserDeviceDto;
import fib.br10.entity.user.UserDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface UserDeviceMapper {

    UserDevice userDeviceDtoToUserDevice(UserDeviceDto userDeviceDto);

    UserDevice userDeviceDtoToUserDevice(@MappingTarget UserDevice userDevice, UserDeviceDto userDeviceDto);

    UserDeviceDto userDeviceUserDeviceToUserDeviceDto(UserDevice userDevice);
}
