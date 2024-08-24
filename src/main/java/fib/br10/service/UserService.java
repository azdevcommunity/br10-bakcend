package fib.br10.service;

import fib.br10.core.dto.UserDetailModel;
import fib.br10.core.entity.EntityStatus;
import fib.br10.dto.auth.request.RegisterRequest;
import fib.br10.entity.user.RoleEnum;
import fib.br10.entity.user.User;
import fib.br10.exception.user.*;
import fib.br10.mapper.UserMapper;
import fib.br10.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService implements UserDetailsService {

    UserRoleService userRoleService;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(UserNotFoundException::new);

        UserDetailModel userDetails = userMapper.userToUserDetails(new UserDetailModel(), user);

        List<GrantedAuthority> authorities = userRoleService.findAllUserRolesAsGrantedAuthority(user.getId());
        userDetails.setAuthorities(authorities);

        return userDetails;

    }

    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findByUserNameOrPhoneNumber(String userNameOrPhoneNumber) {
        return userRepository.findByPhoneNumber(userNameOrPhoneNumber)
                .orElseGet(() -> userRepository.findByUsername(userNameOrPhoneNumber)
                .orElseThrow(UserNotFoundException::new));
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findByPhoneNumberAndStatusNot(String phoneNumber, EntityStatus status) {
        return userRepository.findByPhoneNumberAndStatusNot(phoneNumber, status.getValue())
                .orElseThrow(UserNotFoundException::new);
    }

    public User create(RegisterRequest request) {
        User user = new User();
        user.setStatus(EntityStatus.DE_ACTIVE.getValue());
        user = userMapper.registerDtoToUser(user, request);
        user = save(user);

        userRoleService.addRoleToUser(user.getId(), RoleEnum.SPECIALIST);

        return user;
    }

    public User save(User user) {
        if (Objects.isNull(user.getPhoneNumber())) {
            throw new PhoneNumberRequiredException();
        }

        if (Objects.isNull(user.getPassword())) {
            throw new PasswordRequiredException();
        }

        return userRepository.save(user);
    }

    public Optional<User> checkUserAlreadyExists(String username, String phoneNumber) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent() && userRoleService.isUserSpecialist(user.get().getId())) {
            throw new UserExistSamePhoneNumberException();
        }

        if (userRepository.existsByUsername(username)) {
            throw new UserExistsWithSameUserNameException();
        }


        return user;
    }

    public void existsByIdAndUserRoleSpecialist(Long userId) {
        if (!userRepository.existsById(userId) || !userRoleService.isUserSpecialist(userId)) {
            throw new UserNotFoundException();
        }
    }

    public void existsById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

    public void validateSpecialist(Long specialistId, Long userId) {
        if (!specialistId.equals(userId)) {
            throw new CategoryNotBelongToUserException();
        }
    }
}
