package src.pingit.exceptions;

public class ServerAlreadyExistsException extends RuntimeException {
    public ServerAlreadyExistsException(String message) {
        super(message);
    }
}
