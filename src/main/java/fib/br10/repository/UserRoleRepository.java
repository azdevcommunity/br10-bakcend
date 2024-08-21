package fib.br10.repository;

import fib.br10.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>,
        QuerydslPredicateExecutor<UserRole> {
    List<UserRole> findByUserId(Long userId);
    boolean existsByUserIdAndRoleId(Long userId, Integer roleId);
    boolean existsByRoleId(Integer roleId);
}
