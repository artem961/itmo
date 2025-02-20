package lab5.collection.exceptions;

/**
 * Исключение при попытке создания пустого поля.
 */
public class EmptyFieldException extends RuntimeException {
    public EmptyFieldException() {
        super("Поле не может быть пустым");
    }
}
