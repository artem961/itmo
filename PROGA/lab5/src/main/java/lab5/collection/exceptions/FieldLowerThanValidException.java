package lab5.collection.exceptions;

/**
 * Исключение, когда ввёденное число меньше, чем разрешённая нижняя граница.
 */
public class FieldLowerThanValidException extends RuntimeException {
    /**
     *
     * @param Bound Нижняя граница
     */
    public FieldLowerThanValidException(Number Bound)
    {
        super("Значение должно быть больше чем " + Bound.toString() + ".");
    }
}
