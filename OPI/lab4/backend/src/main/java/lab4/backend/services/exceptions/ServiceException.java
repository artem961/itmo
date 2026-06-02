package lab4.backend.services.exceptions;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true, inherited = false)
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
