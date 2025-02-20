package lab5.collection.exceptions;

/**
 * Исключение при попытке записать в поле null.
 */
public class NullFieldException extends RuntimeException {
    public NullFieldException() {
        super("Значение поля не должно быть null");
    }
}
