package fib.br10.repository;

import fib.br10.entity.specialist.FavoriteSpecialist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteSpecialistRepository extends JpaRepository<FavoriteSpecialist, Long> {
}
