package fib.br10.mapper;

import fib.br10.core.dto.UserDetailModel;
import fib.br10.dto.auth.request.RegisterRequest;
import fib.br10.dto.auth.response.RegisterResponse;
import fib.br10.dto.cache.CacheOtp;
import fib.br10.dto.cache.CacheUser;
import fib.br10.entity.user.User;
import fib.br10.entity.user.RoleEnum;
import org.mapstruct.*;

import java.time.OffsetDateTime;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDetailModel userToUserDetails(@MappingTarget UserDetailModel userDetailModel, User user);

//    @Mapping(target = "userType", source = "userType", qualifiedByName = "mapUserTypeToInt")
    User registerDtoToUser(@MappingTarget User user, RegisterRequest request);

//    @Mapping(target="otpExpireDate",source = "otpExpireDate",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "response.otp", source = "cacheOtp")
    @Mapping(target = "response.otpExpireDate", source = "otpExpireDate")
    RegisterResponse  userToRegisterResponse(@MappingTarget RegisterResponse response, CacheUser user, Integer cacheOtp, OffsetDateTime otpExpireDate);


    @Named("mapUserTypeToInt")
    default Integer mapUserTypeToInt(RoleEnum roleEnum) {
        return roleEnum.getValue();
    }

    @Named("mapIntToUserType")
    default RoleEnum mapIntToUserType(Integer userType) {
        return RoleEnum.fromValue(userType);
    }

    CacheUser registerRequestToCacheUser(RegisterRequest request);

    User cacheUserToEntity(CacheUser cacheUser);
}
    
    
  
 