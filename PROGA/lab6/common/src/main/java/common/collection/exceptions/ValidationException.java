package common.collection.exceptions;

/**
 * Исключение валидации данных.
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
