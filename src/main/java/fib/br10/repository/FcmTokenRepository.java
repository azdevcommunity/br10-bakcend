package fib.br10.repository;

import fib.br10.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
}
