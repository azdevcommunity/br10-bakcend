package fib.br10.repository;

import fib.br10.entity.specialist.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long>,
        QuerydslPredicateExecutor<Speciality> {
    List<Speciality> findByStatus(Integer status);

    boolean existsByNameAndStatus(String name,Integer status);
    boolean existsByNameAndStatusAndIdNot(String name,Integer status,Long id);

    Optional<Speciality> findByIdAndStatus(Long id, Integer status);

    boolean existsByIdAndStatus(Long id, Integer status);

}
