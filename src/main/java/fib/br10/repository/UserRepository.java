package fib.br10.repository;

import fib.br10.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndStatusNot(String phoneNumber, Integer status);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByUsername(String userName);

    Optional<User> findByUsername(String username);

//    boolean existsByIdAndUserType(Long userId,Integer userType);
}