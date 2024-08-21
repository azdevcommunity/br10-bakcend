package fib.br10.repository;

import fib.br10.entity.specialist.SpecialistAvailability;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface SpecialistAvailabilityRepository extends JpaRepository<SpecialistAvailability, Long>{

    Boolean existsBySpecialistUserIdAndDayOfWeek(Long specialistUserId, Integer dayOfWeek);
    List<SpecialistAvailability>  findAllBySpecialistUserIdAndStatus(Long specialistUserId, Integer status);

}