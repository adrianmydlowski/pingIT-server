package src.pingit.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import src.pingit.enums.Status;
import src.pingit.exceptions.ServerAlreadyExistsException;
import src.pingit.exceptions.ServerNotFoundException;
import src.pingit.model.Server;
import src.pingit.repository.ServerRepository;
import src.pingit.service.ServerService;
import src.pingit.util.ServerUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {
    private static final int TIMEOUT = 3000;

    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        log.info("Creating a new server: {}", server.getName());
        Optional<Server> serverOpt = serverRepository.findByIpAddress(server.getIpAddress());
        if (serverOpt.isPresent()) {
            throw new ServerAlreadyExistsException(String.format("Server with ipAddress=%s already exists.", server.getIpAddress()));
        }
        server.setImageUrl(ServerUtil.getRandomServerImage());
        return serverRepository.save(server);
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public boolean delete(Long id) {
        log.info("Deleting server with id: {}", id);
        serverRepository.deleteById(id);
        return true;
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        return serverRepository.findById(id)
                .orElseThrow(() -> new ServerNotFoundException(String.format("Server with id=%d not found.", id)));
    }

    @Override
    public List<Server> getAll(Pageable pageable) {
        log.info("Fetching all servers");
        return serverRepository.findAll(pageable).getContent();
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Sending ping request to: {}", ipAddress);

        Optional<Server> serverOpt = serverRepository.findByIpAddress(ipAddress);
        if (serverOpt.isEmpty()) {
            log.info("Server with IP {} not found", ipAddress);
            return null;
        }

        boolean reachable = InetAddress.getByName(ipAddress).isReachable(TIMEOUT);
        log.info(reachable ? "Host is reachable" : "Host is NOT reachable");

        Server server = serverOpt.get();
        Status actualStatus = server.getStatus();
        Status newStatus = reachable ? Status.SERVER_UP : Status.SERVER_DOWN;

        // if server status did NOT change, return server
        if (Objects.equals(actualStatus, newStatus)) {
            return server;
        }

        // update server status if changed
        server.setStatus(newStatus);
        serverRepository.save(server);

        return server;
    }
}
