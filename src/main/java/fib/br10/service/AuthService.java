package fib.br10.service;

import fib.br10.core.dto.Token;
import fib.br10.core.dto.UserDetailModel;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.utility.*;
import fib.br10.dto.auth.request.*;
import fib.br10.dto.auth.response.OtpResponse;
import fib.br10.dto.auth.response.RegisterResponse;
import fib.br10.dto.cache.CacheOtp;
import fib.br10.dto.specialist.specialistprofile.request.CreateSpecialistProfileRequest;
import fib.br10.entity.user.User;
import fib.br10.exception.auth.ConfirmPasswordNotMatchException;
import fib.br10.exception.token.DecryptException;
import fib.br10.exception.token.EncryptException;
import fib.br10.exception.token.JwtAtBlackListException;
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


    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        //bu api sirf speciailst oaraq register elemek ucundu
        //bu nomre varmi varsa clientdimi yoxsa specialistdimi
        //specialistdise xeta at
        //username varsa phone numberde eynidise problem deyl amma ferqlidise xeta at

        User user = userService.checkUserAlreadyExists(request.getUsername(),
                request.getPhoneNumber()
        ).orElse(null);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ConfirmPasswordNotMatchException();
        }
        specialityService.checkSpecialityExists(request.getSpecialityId());

//        if (request.getUserType().equals(UserRoleEnum.SPECIALIST.getValue()) && Objects.isNull(request.getSpeciality())) {
//            throw new BaseException(Messages.SPECIALITY_REQUIRED);
//        }
        if (Objects.isNull(user)) {
            user = userService.create(request);
        }

        CacheOtp cacheOtp = otpService.add(user.getId());

        registerSpecialist(request, user);


        //TODO: send otp to phone number from sms

        RegisterResponse response = userMapper.userToRegisterResponse(new RegisterResponse(), user);
        response.setOtp(cacheOtp.getOtp());
        response.setOtpExpireDate(cacheOtp.getOtpExpireDate());

        return response;
    }

    public Token activateUserVerifyOtp(VerifyOtpRequest request) {
        User user = userService.findByPhoneNumberAndStatusNot(request.getPhoneNumber(),
                EntityStatus.DELETED
        );

        otpService.verifyOtp(user.getId(), request.getOtp());

        user.setStatus(EntityStatus.ACTIVE.getValue());

        userService.save(user);

        return tokenService.get(user);
    }

    public OtpResponse getOtp(GetOtpRequest request) {
        User user = userService.findByUserNameOrPhoneNumber(request.getPhoneNumberOrUsername());

        CacheOtp cacheOtp = otpService.add(user.getId());

//        userService.save(user);

        return new OtpResponse(cacheOtp.getOtp(), cacheOtp.getOtpExpireDate());
    }

    public Token login(LoginRequest request) {
        User user = userService.findByUserNameOrPhoneNumber(request.getPhoneNumberOrUsername());

        if (!user.getStatus().equals(EntityStatus.ACTIVE.getValue())) {
            throw new UserNotActiveException();
        }

        if (!request.getPassword().equals(user.getPassword())) {
            //CHANGE ERROR
            throw new ConfirmPasswordNotMatchException();
        }

        Token token = tokenService.get(user);

        return token;
    }

    public String resetPasswordVerifyOtp(VerifyOtpRequest request) {
        User user = userService.findByPhoneNumberAndStatusNot(request.getPhoneNumber(), EntityStatus.DELETED);

        otpService.verifyOtp(user.getId(), request.getOtp());

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

        return tokenService.get(user);
    }

    public void logout() {
        tokenService.addTokenToBlackList();
    }

    protected void registerSpecialist(RegisterRequest request, User user) {
        specialistProfileService.create(CreateSpecialistProfileRequest.builder()
                .specialistUserId(user.getId())
                .specialityId(request.getSpecialityId())
                .build());
        specialistAvailabilityService.addWeekendAvailability(user.getId());
    }

}
