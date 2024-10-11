package fib.br10.repository;

import fib.br10.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);


    Optional<User> findByPhoneNumberAndStatusNot(String phoneNumberOrUsername, Integer status);

    Optional<User> findByPhoneNumberAndAndStatus(String phoneNumber , Integer status);

    Optional<User> findByPhoneNumberAndStatus(String phoneNumber, Integer status);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByUsernameAndStatus(String userName,Integer status);

    Optional<User> findByUsername(String username);

//    boolean existsByIdAndUserType(Long userId,Integer userType);
}