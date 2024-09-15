package fib.br10.repository;

import fib.br10.entity.user.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    Optional<UserDevice> findByDeviceIdAndUserId(String deviceId,Long userId);
}
