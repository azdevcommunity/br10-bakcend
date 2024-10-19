package fib.br10.service;

import fib.br10.configuration.SecurityEnv;
import fib.br10.core.dto.Token;
import fib.br10.core.dto.UserDetailModel;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.core.utility.*;
import fib.br10.dto.auth.request.*;
import fib.br10.dto.auth.response.OtpResponse;
import fib.br10.dto.auth.response.RegisterResponse;
import fib.br10.dto.cache.CacheOtp;
import fib.br10.dto.cache.CacheUser;
import fib.br10.dto.specialist.specialistprofile.request.CreateSpecialistProfileRequest;
import fib.br10.dto.userdevice.request.UserDeviceDto;
import fib.br10.entity.user.RoleEnum;
import fib.br10.entity.user.User;
import fib.br10.enumeration.RegisterType;
import fib.br10.exception.auth.ConfirmPasswordNotMatchException;
import fib.br10.exception.token.DecryptException;
import fib.br10.exception.token.EncryptException;
import fib.br10.exception.token.TokenNotValidException;
import fib.br10.exception.user.UserNotActiveException;
import fib.br10.mapper.UserMapper;
import fib.br10.service.abstracts.CacheService;
import fib.br10.utility.CacheKeys;
import fib.br10.utility.JwtService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {

    JwtService jwtService;
    EncryptionUtil encryptionUtil;
    SpecialistProfileService specialistProfileService;
    OtpService otpService;
    UserService userService;
    UserMapper userMapper;
    TokenService tokenService;
    SpecialistAvailabilityService specialistAvailabilityService;
    SpecialityService specialityService;
    UserDeviceService userDeviceService;
    CacheService<String, Integer> cacheService;
    UserRoleService userRoleService;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        //username varsa phone numberde eynidise problem deyl amma ferqlidise xeta at
        //user insert edirem deactive;
//        validateUserNotBlocked(provider.getIpAddress());
//        validateRateLimit(securityEnv.getAuthRateLimit().register(), CacheKeys.REGISTER_TRY_COUNT + provider.getIpAddress());

        userService.checkUserAlreadyExists(request.getUsername(),
                request.getPhoneNumber()
        );

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ConfirmPasswordNotMatchException();
        }

        boolean isSpecialist = RegisterType.SPECIALIST.equals(RegisterType.fromValue(request.getRegisterType()));

        if (isSpecialist) {
            specialityService.checkSpecialityExists(request.getSpecialityId());
        }

//        if (Objects.isNull(user)) {
//            //add cache
           CacheUser cacheUser = userService.addUserToCache(request);
//        }

        CacheOtp cacheOtp = otpService.create(request.getPhoneNumber());

//        if (isSpecialist) {
//            registerSpecialist(request, user);
//        }

        //TODO: send otp to phone number from sms
        return userMapper.userToRegisterResponse(new RegisterResponse(), cacheUser, cacheOtp.getOtp(), cacheOtp.getOtpExpireDate());
    }

    @Transactional
    public Token activateUserVerifyOtp(ActivateUserVerifyOtpRequest request) {
        CacheUser cacheUser = userService.findUserFromCache(request.getPhoneNumber());
        userService.checkUserAlreadyExists(cacheUser.getUsername(),
                cacheUser.getPhoneNumber()
        );

        otpService.verify(cacheUser.getPhoneNumber(), request.getOtp());

        User user = userMapper.cacheUserToEntity(cacheUser);
        user.setStatus(EntityStatus.ACTIVE.getValue());
        user = userService.save(user);

        boolean isSpecialist = RegisterType.SPECIALIST.equals(
                RegisterType.fromValue(cacheUser.getRegisterType())
        );

        if (isSpecialist) {
            registerSpecialist(cacheUser.getSpecialityId(), user);
            userRoleService.addRoleToUser(user.getId(), RoleEnum.SPECIALIST);
        }

        UserDeviceDto deviceDto = request.getUserDeviceDto();
        deviceDto.setUserId(user.getId());
        deviceDto = userDeviceService.create(deviceDto);
        return tokenService.get(user, deviceDto);
    }

    public OtpResponse getOtp(GetOtpRequest request) {
//        User user = userService.findByUserNameOrPhoneNumber(request.getPhoneNumberOrUsername());
        CacheOtp cacheOtp = otpService.create(request.getPhoneNumber());
        return new OtpResponse(cacheOtp.getOtp(), cacheOtp.getOtpExpireDate());
    }

    @Transactional
    public Token login(LoginRequest request) {
//        validateUserNotBlocked(provider.getIpAddress());
//        validateRateLimit(securityEnv.getAuthRateLimit().login(), CacheKeys.LOGIN_TRY_COUNT + provider.getIpAddress());

        User user = userService.findByUserNameOrPhoneNumber(request.getPhoneNumberOrUsername());

        if (!user.getStatus().equals(EntityStatus.ACTIVE.getValue())) {
            throw new UserNotActiveException();
        }

        if (!request.getPassword().equals(user.getPassword())) {
            //CHANGE ERROR
            throw new ConfirmPasswordNotMatchException();
        }

        UserDeviceDto userDeviceDto = request.getUserDeviceDto();
        userDeviceDto.setUserId(user.getId());
        userDeviceDto = userDeviceService.update(userDeviceDto);

        //send notification to all user devices with  CompletableFuture.runAsync
        return tokenService.get(user, userDeviceDto);
    }

    public String resetPasswordVerifyOtp(VerifyOtpRequest request) {
        User user = userService.findByPhoneNumberAndStatusNot(request.getPhoneNumber(), EntityStatus.DELETED);

        otpService.verify(user.getPhoneNumber(), request.getOtp());

        String payload = user.getId() + "*" + DateUtil.getCurrentDateTime().plusMinutes(5);

        String key = encryptionUtil.encrypt(payload)
                .orElseThrow(EncryptException::new);

        userService.save(user);

        return key;
    }

    public Long resetPassword(ResetPasswordRequest request) {
        String key = encryptionUtil.decrypt(request.getToken())
                .orElseThrow(DecryptException::new);

        String[] parts = key.split("\\*");

        String userIdFromToken = parts[0];
        OffsetDateTime expireDate = OffsetDateTime.parse(parts[1]);

        if (DateUtil.isBefore(expireDate)) {
            throw new TokenNotValidException();
        }

        User user = userService.findById(Long.valueOf(userIdFromToken));

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ConfirmPasswordNotMatchException();
        }

        user.setPassword(request.getPassword());
        userService.save(user);

        return user.getId();
    }

    public Token refreshToken(RefreshTokenRequest request) {
        tokenService.validateTokenExistsOnBlackList(request.getRefreshToken());

        String phoneNumber = jwtService.extractUsername(request.getRefreshToken());

        User user = userService.findByPhoneNumber(phoneNumber);

        UserDetails userDetails = userMapper.userToUserDetails(new UserDetailModel(), user);

        tokenService.validateToken(request.getRefreshToken(), userDetails);

        tokenService.addTokenToBlackList(request.getRefreshToken());

        Long deviceId = jwtService.extractClaim(request.getRefreshToken(), ClaimTypes.TOKEN_ID);

        UserDeviceDto userDeviceDto = userDeviceService.getUserDevice(deviceId);

        return tokenService.get(user, userDeviceDto);
    }

    public void logout() {
        tokenService.addTokenToBlackList();
    }

    @Transactional
    protected void registerSpecialist(Long specialistId, User user) {
        specialistProfileService.create(CreateSpecialistProfileRequest.builder()
                .specialistUserId(user.getId())
                .specialityId(specialistId)
                .build());
        specialistAvailabilityService.addWeekendAvailability(user.getId());
    }

    private void validateUserNotBlocked(String ipAddress) {
        Integer userBlocked = cacheService.get(CacheKeys.TEMPORARY_BLOCKED_USERS + ipAddress);
        if (Objects.nonNull(userBlocked)) {
            throw new BaseException("user uje blocklanib");
        }
    }

    private void validateRateLimit(SecurityEnv.Limitation limitation, String key) {
        Integer loginCount = cacheService.get(key);
        if (limitation.maxAllowedAttemps().equals(loginCount)) {
            throw new BaseException("qaqa besdi dahaa poxunu cixartma");
        }
        //TODO: null olanda artirirmi baxmaq lazimdi
        cacheService.increment(key);
    }
}
