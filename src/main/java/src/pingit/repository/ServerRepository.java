package src.pingit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.pingit.model.Server;

import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, Long> {
    Optional<Server> findByIpAddress(String ipAddress);
}
