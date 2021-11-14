package src.pingit.exceptions;

public class ServerNotFoundException extends RuntimeException {
    public ServerNotFoundException(String message) {
        super(message);
    }
}
