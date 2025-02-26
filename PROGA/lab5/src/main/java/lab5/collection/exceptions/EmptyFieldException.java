package lab5.collection.exceptions;

/**
 * Исключение при попытке создания пустого поля.
 */
public class EmptyFieldException extends ValidationException {
    public EmptyFieldException(String field) {
        super("Поле " + field + "не может быть пустым");
    }
}
