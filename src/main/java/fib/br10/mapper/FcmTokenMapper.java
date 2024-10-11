package fib.br10.mapper;

import fib.br10.dto.fcmtoken.request.CreateFcmTokenRequest;
import fib.br10.entity.FcmToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FcmTokenMapper {
    @Mapping(target = "userId", source = "userId")
    FcmToken createFcmTokenToFcmToken(CreateFcmTokenRequest fcmTokenRequest, Long userId);
}
