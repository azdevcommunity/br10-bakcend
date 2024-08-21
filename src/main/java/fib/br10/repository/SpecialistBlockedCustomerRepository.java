package fib.br10.repository;

import fib.br10.entity.specialist.SpecialistBlockedCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialistBlockedCustomerRepository extends JpaRepository<SpecialistBlockedCustomer, Long>{
    boolean existsBySpecialistUserIdAndCustomerUserIdAndStatus(Long specialistUserId, Long customerUserId, Integer status);
    Optional<SpecialistBlockedCustomer> findBySpecialistUserIdAndCustomerUserIdAndStatus(Long specialistUserId, Long customerUserId, Integer status);

    Optional<SpecialistBlockedCustomer> findByCustomerUserIdAndSpecialistUserIdAndStatus(Long userId,Long specialistUserId,Integer status );
}