package fib.br10.service;

//import fib.br10.entity.user.UserRole;

import fib.br10.entity.user.RoleEnum;
import fib.br10.entity.user.UserRole;
import fib.br10.repository.UserRoleRepository;
import fib.br10.utility.PrefixUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserRoleService {

    UserRoleRepository userRoleRepository;

    public boolean isUserHaveRole(Long userId, RoleEnum role) {
        return userRoleRepository.existsByUserIdAndRoleId(userId, role.getValue());
    }

    public boolean isUserSpecialist(Long userId) {
        return isUserHaveRole(userId, RoleEnum.SPECIALIST);
    }

    @Transactional
    public void addRoleToUser(Long userId, RoleEnum role) {
        if (Objects.isNull(userId) || Objects.isNull(role)) {
            throw new IllegalArgumentException("userId and role cannot be null");
        }
        UserRole userRole = new UserRole(userId, role.getValue());
        userRoleRepository.save(userRole);
    }

    public List<String> findAllUserRoles(Long userId) {
        return userRoleRepository.findByUserId(userId)
                .stream()
                .map(role -> RoleEnum.fromValue(role.getRoleId()).toString())
                .toList();
    }


    public List<GrantedAuthority> findAllUserRolesAsGrantedAuthority(Long userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(role -> new SimpleGrantedAuthority(PrefixUtil.ROLE + RoleEnum.fromValue(role.getRoleId())))
                .collect(Collectors.toList());
    }
}
