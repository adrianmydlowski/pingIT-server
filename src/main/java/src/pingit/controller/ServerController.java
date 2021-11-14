package src.pingit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import src.pingit.enums.Status;
import src.pingit.model.Server;
import src.pingit.model.dto.ResponseDto;
import src.pingit.service.ServerService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {
    private final ServerService serverService;

    @GetMapping("/all")
    public ResponseEntity<ResponseDto> getServers() {
        return ResponseEntity.ok(getResponse(of("servers", serverService.getAll(PageRequest.of(0, 30))),
                "Servers retrieved", OK));
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<ResponseDto> pingServer(@PathVariable String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok(getResponse(of("server", server),
                Objects.equals(server.getStatus(), Status.SERVER_UP) ? "Ping success" : "Ping failed", OK));
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveServer(@RequestBody @Valid Server server) {
        return ResponseEntity.ok(getResponse(of("server", serverService.create(server)),
                "Server created", CREATED));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseDto> getServer(@PathVariable Long id) {
        return ResponseEntity.ok(getResponse(of("server", serverService.get(id)),
                "Server retrieved", OK));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteServer(@PathVariable Long id) {
        return ResponseEntity.ok(getResponse(of("deleted", serverService.delete(id)),
                "Server deleted", OK));
    }

    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:images/" + fileName);
        return Files.readAllBytes(file.toPath());
    }

    private ResponseDto getResponse(Map<?, ?> data, String message, HttpStatus status) {
        return ResponseDto.builder()
                .timeStamp(now())
                .data(data)
                .message(message)
                .status(status)
                .statusCode(status.value())
                .build();
    }
}
