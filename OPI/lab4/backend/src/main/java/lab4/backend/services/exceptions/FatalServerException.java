package lab4.backend.services.exceptions;

public class FatalServerException extends RuntimeException {
    public FatalServerException(String message) {
        super(message);
    }

    public FatalServerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
