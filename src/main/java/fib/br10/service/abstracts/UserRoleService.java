package fib.br10.service.abstracts;


import fib.br10.entity.user.RoleEnum;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserRoleService {

    boolean isUserHaveRole(Long userId, RoleEnum role);

    boolean isUserSpecialist(Long userId);

    void addRoleToUser(Long userId, RoleEnum role);

    List<String> findAllUserRoles(Long userId);

    List<GrantedAuthority> findAllUserRolesAsGrantedAuthority(Long userId);
}
