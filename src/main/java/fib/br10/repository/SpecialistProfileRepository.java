package fib.br10.repository;

import fib.br10.entity.specialist.SpecialistProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialistProfileRepository extends JpaRepository<SpecialistProfile, Long> {

    Boolean existsBySpecialistUserId(Long userId);
    boolean existsBySpecialityId(Long id);

    Optional<SpecialistProfile> findBySpecialistUserId(Long userId);
}