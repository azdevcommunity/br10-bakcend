package fib.br10.service;

import fib.br10.core.dto.Token;
import fib.br10.core.dto.UserDetailModel;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.*;
import fib.br10.dto.auth.request.*;
import fib.br10.dto.auth.response.OtpResponse;
import fib.br10.dto.auth.response.RegisterResponse;
import fib.br10.dto.cache.CacheOtp;
import fib.br10.dto.specialist.specialistprofile.request.CreateSpecialistProfileRequest;
import fib.br10.dto.userdevice.request.UserDeviceDto;
import fib.br10.entity.user.User;
import fib.br10.enumeration.RegisterType;
import fib.br10.exception.auth.ConfirmPasswordNotMatchException;
import fib.br10.exception.token.DecryptException;
import fib.br10.exception.token.EncryptException;
import fib.br10.exception.token.TokenNotValidException;
import fib.br10.exception.user.UserNotActiveException;
import fib.br10.mapper.UserMapper;
import fib.br10.utility.JwtService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

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
    RequestContextProvider provider;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        //username varsa phone numberde eynidise problem deyl amma ferqlidise xeta at
        //user insert edirem deactive;
        User user = userService.checkUserAlreadyExists(request.getUsername(),
                request.getPhoneNumber()
        ).orElse(null);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ConfirmPasswordNotMatchException();
        }

        boolean isSpecialist = RegisterType.SPECIALIST.equals(RegisterType.fromValue(request.getRegisterType()));

        if (isSpecialist) {
            specialityService.checkSpecialityExists(request.getSpecialityId());
        }

        if (Objects.isNull(user)) {
            user = userService.create(request);
        }

        CacheOtp cacheOtp = otpService.create(user.getId());

        if (isSpecialist) {
            registerSpecialist(request, user);
        }

        //TODO: send otp to phone number from sms
        RegisterResponse response = userMapper.userToRegisterResponse(new RegisterResponse(), user);
        response.setOtp(cacheOtp.getOtp());
        response.setOtpExpireDate(cacheOtp.getOtpExpireDate());
        return response;
    }

    @Transactional
    public Token activateUserVerifyOtp(ActivateUserVerifyOtpRequest request) {
        User user = userService.findUser(request.getPhoneNumber(), UUID.fromString(provider.getActivityId()),
                EntityStatus.DE_ACTIVE
        );

        if (EntityStatus.ACTIVE.getValue().equals(user.getStatus())) {
            throw new BaseException("user already activated");
        }

        otpService.verify(user.getId(), request.getOtp());
        user.setStatus(EntityStatus.ACTIVE.getValue());
        userService.save(user);
        UserDeviceDto deviceDto = request.getUserDeviceDto();
        deviceDto.setUserId(user.getId());
        deviceDto = userDeviceService.create(deviceDto);
        return tokenService.get(user, deviceDto);
    }

    public OtpResponse getOtp(GetOtpRequest request) {
        User user = userService.findByUserNameOrPhoneNumber(request.getPhoneNumberOrUsername());
        CacheOtp cacheOtp = otpService.create(user.getId());
        return new OtpResponse(cacheOtp.getOtp(), cacheOtp.getOtpExpireDate());
    }

    @Transactional
    public Token login(LoginRequest request) {
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

        otpService.verify(user.getId(), request.getOtp());

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
    protected void registerSpecialist(RegisterRequest request, User user) {
        specialistProfileService.create(CreateSpecialistProfileRequest.builder()
                .specialistUserId(user.getId())
                .specialityId(request.getSpecialityId())
                .build());
        specialistAvailabilityService.addWeekendAvailability(user.getId());
    }
}
