package lab5.collection.exceptions;

/**
 * Исключение, когда ввёденное число меньше, чем разрешённая нижняя граница.
 */
public class FieldLowerThanValidException extends ValidationException {
    /**
     *
     * @param Bound Нижняя граница
     */
    public FieldLowerThanValidException(String field, Number Bound)
    {
        super("Значение поля " + field + " должно быть больше чем " + Bound.toString() + ".");
    }
}
