package src.pingit.service;

import org.springframework.data.domain.Pageable;
import src.pingit.model.Server;

import java.io.IOException;
import java.util.List;

public interface ServerService {
    Server create(Server server);

    Server update(Server server);

    boolean delete(Long id);

    Server get(Long id);

    List<Server> getAll(Pageable pageable);

    Server ping(String ipAddress) throws IOException;
}
