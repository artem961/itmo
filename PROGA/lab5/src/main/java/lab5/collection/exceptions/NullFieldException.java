package lab5.collection.exceptions;

/**
 * Исключение при попытке записать в поле null.
 */
public class NullFieldException extends ValidationException {
    public NullFieldException(String field) {
        super("Значение поля " + field + " не должно быть null!");
    }
}
