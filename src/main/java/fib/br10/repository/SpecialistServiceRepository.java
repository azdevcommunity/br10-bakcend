package fib.br10.repository;

import fib.br10.entity.specialist.SpecialistService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialistServiceRepository extends JpaRepository<SpecialistService, Long>,
        QuerydslPredicateExecutor<SpecialistService>{

    Boolean existsByNameAndIdNot(String name, Long id);

    Boolean existsByName(String name);

    Optional<SpecialistService> findByIdAndSpecialistUserId(Long id, Long specialistUserId);

}