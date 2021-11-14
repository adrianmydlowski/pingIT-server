package src.pingit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import src.pingit.enums.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@NoArgsConstructor
public class Server {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    @NotEmpty(message = "The name can not be empty")
    private String name;
    @Column(unique = true, nullable = false)
    @NotEmpty(message = "The IP address can not be empty")
    private String ipAddress;
    private String memory;
    private String type;
    private String imageUrl;
    private Status status;
}

